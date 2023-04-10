package com.bntech.imperio.instances.service.impl;

import com.bntech.imperio.instances.data.dto.InstanceDetailsDbQueryDto;
import com.bntech.imperio.instances.data.dto.InstanceLinodeResponseDto;
import com.bntech.imperio.instances.data.model.*;
import com.bntech.imperio.instances.data.model.repository.*;
import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import com.bntech.imperio.instances.service.InstanceService;
import com.bntech.imperio.instances.service.InstanceSubcomponentsService;
import com.bntech.imperio.instances.service.SingleInstanceService;
import com.bntech.imperio.instances.service.util.TypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.UnknownHostException;
import java.util.List;

import static com.bntech.imperio.instances.service.util.TypeConverter.inetListToStringList;

@Component
@Slf4j
public class InstanceServiceImpl implements InstanceService {
    private final InstanceRepo instances;
    private final SingleInstanceService singleInstances;
    private final InstanceSubcomponentsService subcomponentsService;

    @Autowired
    public InstanceServiceImpl(InstanceRepo instances,
                               SingleInstanceServiceImpl singleInstances,
                               InstanceSubcomponentsServiceImpl subcomponentsService) {
        this.instances = instances;
        this.singleInstances = singleInstances;
        this.subcomponentsService = subcomponentsService;
    }

    @Override
    public Mono<InstanceLinodeResponseDto> getInstanceDetails(Mono<String> id) {
        return id.transform(TypeConverter::monoStringToLong)
                .transform(instances::allAboutOne)
                .transform(this::queryToResponse);
    }

    @Override
    public Mono<ServerResponse> initDeployment(Mono<InstanceCreateRequest> instanceRequest) {
        return instanceRequest.flatMap(details -> switch (details.getRequestType()) {
            //todo: There is a parse because we need diff requests for diff instances
            case regular -> singleInstances.deploy(details);
            case kubernetesHost -> singleInstances.deploy(details);
            case kubernetesWorker -> singleInstances.deploy(details);
        });
    }

    @Override
    public Flux<Instance> handleUpsert(List<InstanceLinodeResponseDto> linodeResponseDtos) {
        return Flux.fromIterable(linodeResponseDtos)
                .flatMap(dto -> {
                    try {
                        return instances.allAboutOne(Mono.just(dto.id()))
                                .flatMap(subcomponentsService::allAboutOneToSubcomponents)
                                .flatMap(subcomponentsService::upsertInstanceSubscomponents)
                                .switchIfEmpty(upsertCreate(dto))
                                .log()
                                .onErrorResume(e -> {
                                    log.error("Error while upserting instance: {} / {} / {}", dto.id(), e.getLocalizedMessage(), e.getClass().getName());
                                    return Mono.empty();
                                });
                    } catch (UnknownHostException e) {
                        return Flux.error(new RuntimeException(e));
                    }
                });
    }

    private Mono<Instance> upsertCreate(InstanceLinodeResponseDto dto) throws UnknownHostException {
        log.info("Creating upsert: {}", dto.id());
        return subcomponentsService.createAll(dto)
                .flatMap(tuple -> Mono.just(Instance.builder()
                        .alert(tuple.getT1().id())
                        .address(tuple.getT2().id())
                        .spec(tuple.getT3().id())
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
                        .status(dto.status())
                        .tags(dto.tags())
                        .type(dto.type())
                        .updated(dto.updated())
                        .watchdog_enabled(dto.watchdog_enabled())
                        .build()))
                .flatMap(instances::save);
    }

    private Mono<InstanceLinodeResponseDto> queryToResponse(Mono<InstanceDetailsDbQueryDto> dto) {
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
}
