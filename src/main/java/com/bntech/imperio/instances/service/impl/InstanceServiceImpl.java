package com.bntech.imperio.instances.service.impl;

import com.bntech.imperio.instances.data.dto.InstanceDetailsDbQueryDto;
import com.bntech.imperio.instances.data.dto.InstanceLinodeResponseDto;
import com.bntech.imperio.instances.data.model.Instance;
import com.bntech.imperio.instances.data.model.repository.InstanceRepo;
import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import com.bntech.imperio.instances.data.object.types.InstanceStatus;
import com.bntech.imperio.instances.service.InstanceService;
import com.bntech.imperio.instances.service.InstanceSubcomponentsService;
import com.bntech.imperio.instances.service.SingleInstanceService;
import com.bntech.imperio.instances.service.util.TypeConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.bntech.imperio.instances.service.util.TypeConverter.inetListToStringList;

@Component
@Slf4j
public class InstanceServiceImpl implements InstanceService {
    private final InstanceRepo instances;
    private final SingleInstanceService singleInstances;
    private final InstanceSubcomponentsService subcomponentsService;
    private final TaskExecutor taskExecutor;

    @Autowired
    public InstanceServiceImpl(InstanceRepo instances,
                               SingleInstanceServiceImpl singleInstances,
                               InstanceSubcomponentsServiceImpl subcomponentsService, TaskExecutor taskExecutor) {
        this.instances = instances;
        this.singleInstances = singleInstances;
        this.subcomponentsService = subcomponentsService;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public Mono<InstanceDetailsDbQueryDto> getInstanceDetails(Mono<String> id) {
        return id.transform(TypeConverter::monoStringToLong)
                .transform(instances::allAboutOne);
    }

    @Override
    public Mono<ServerResponse> handleInstanceCreateRequest(Mono<InstanceCreateRequest> instanceRequest) {
        return instanceRequest.flatMap(details -> switch (details.getRequestType()) {
            //todo: There is a parse because we need diff requests for diff instances
            case regular -> singleInstances.deploy(details);
            case kubernetesHost -> singleInstances.deploy(details);
            case kubernetesWorker -> singleInstances.deploy(details);
        });
    }

    @Override
    public Flux<InstanceLinodeResponseDto> upsertLinodeData(List<InstanceLinodeResponseDto> linodeResponseDtos) {
        taskExecutor.execute(new UpsertPurgerThread(linodeResponseDtos));
        return Flux.fromIterable(linodeResponseDtos)
                .flatMap(dto -> {
                    log.info("Upserting instance: {}", dto.id());

                    return getInstanceDetails(Mono.just(dto.id().toString()))
                            .flatMap(fromDatabase -> upsertUpdater(dto, fromDatabase))
                            .switchIfEmpty(this.upsertCreator(dto));
                });
    }

    @Override
    public Mono<ServerResponse> deleteInstance(String id) {
        return singleInstances.delete(Mono.just(Long.valueOf(id)));
    }

    @Override
    public Mono<InstanceLinodeResponseDto> queryToResponse(Mono<InstanceDetailsDbQueryDto> dto) {
        return dto.flatMap(detailsDto -> Mono.just(new InstanceLinodeResponseDto(
                InstanceLinodeResponseDto.InstanceLinodeReplyAlertDto.builder()
                        .cpu(detailsDto.getI_alert_cpu())
                        .io(detailsDto.getI_alert_io())
                        .network_in(detailsDto.getI_alert_network_in())
                        .network_out(detailsDto.getI_alert_network_out())
                        .transfer_quota(detailsDto.getI_alert_transfer_quota())
                        .build(),
                InstanceLinodeResponseDto.InstanceLinodeReplyBackupDto.builder()
                        .available(detailsDto.getInstance_backup_available())
                        .enabled(detailsDto.getInstance_backup_enabled())
                        .last_successful(detailsDto.getInstance_last_successful())
                        .schedule(InstanceLinodeResponseDto.InstanceLinodeReplyBackupScheduleDto.builder()
                                .day(detailsDto.getInstance_backup_day())
                                .window(detailsDto.getInstance_backup_window()).build())
                        .build(),
                detailsDto.getInstance_created(),
                detailsDto.getInstance_group(),
                detailsDto.getInstance_host_uuid(),
                detailsDto.getInstance_hypervisor(),
                Long.parseLong(detailsDto.getInstance_id().toString()),
                detailsDto.getInstance_image(),
                inetListToStringList(detailsDto.getI_ip_v4()),
                detailsDto.getI_ip_v6().toString(),
                detailsDto.getInstance_label(),
                InstanceLinodeResponseDto.InstanceLinodeReplySpecsDto.builder()
                        .disk(detailsDto.getI_spec_disk())
                        .memory(detailsDto.getI_spec_memory())
                        .transfer(detailsDto.getI_spec_transfer())
                        .vcpus(detailsDto.getI_spec_vcpu()).build(),
                detailsDto.getInstance_status(),
                detailsDto.getInstance_tags(),
                detailsDto.getInstance_type(),
                detailsDto.getInstance_updated(),
                detailsDto.getInstance_watchdog_enable(),
                detailsDto.getInstance_region()))
        );
    }

    private Mono<InstanceLinodeResponseDto> upsertCreator(InstanceLinodeResponseDto dto) {
        try {
            return subcomponentsService.createAll(dto)
                    .flatMap(tuple -> {
                        log.info("subcomponents created address toString: address: {}, alert: {}", tuple.getT2(), tuple.getT1());

                        return Mono.just(Instance.builder()
                                .alert(tuple.getT1().getId())
                                .address(tuple.getT2().getId())
                                .spec(tuple.getT3().getId())
                                .id(dto.id())
                                .region(dto.region())
                                .available(dto.backups().available())
                                .enabled(dto.backups().enabled())
                                .last_successful(dto.backups().last_successful())
                                .backup_day(dto.backups().schedule().day())
                                .window(dto.backups().schedule().window())
                                .created(dto.created())
                                .group(dto.group())
                                .host_uuid(dto.host_uuid())
                                .hypervisor(dto.hypervisor())
                                .image(dto.image())
                                .label(dto.label())
                                .status(InstanceStatus.valueOf(dto.status().toUpperCase()))
                                .tags(dto.tags())
                                .type(dto.type())
                                .updated(dto.updated())
                                .watchdog_enabled(dto.watchdog_enabled())
                                .build()
                                .newInstance());
                    })
                    .flatMap(instances::save)
                    .then(Mono.just(dto));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private Mono<InstanceLinodeResponseDto> upsertUpdater(InstanceLinodeResponseDto update, InstanceDetailsDbQueryDto current) {
        if (current.getInstance_id() == null
                || current.getI_ip_id() == null
                || current.getI_alert_id() == null
                || current.getI_spec_id() == null) {

            return Mono.empty();
        }

        return Mono.just(update);
    }

    @AllArgsConstructor
    class UpsertPurgerThread implements Runnable {
        List<InstanceLinodeResponseDto> linodeResponseDtos;

        @Override
        public void run() {
            List<Long> ids = new ArrayList<>();
            linodeResponseDtos.forEach(inputDto -> {
                log.info("foreach: {}", inputDto.id());
                ids.add(inputDto.id());
            });

            if (!ids.isEmpty()) {
                instances.deleteByNotInIds(ids).subscribe();
            } else {
                log.warn("No records returned. Purging db");
                instances.deleteAll().subscribe();
            }
        }
    }
}
