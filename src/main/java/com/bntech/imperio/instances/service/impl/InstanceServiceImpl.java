package com.bntech.imperio.instances.service.impl;

import com.bntech.imperio.instances.data.dto.DatabaseInstanceDetailsDto;
import com.bntech.imperio.instances.data.dto.UserDetailsResponseDto;
import com.bntech.imperio.instances.data.model.repository.InstanceRepo;
import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import com.bntech.imperio.instances.handler.ErrorHandler;
import com.bntech.imperio.instances.service.InstanceService;
import com.bntech.imperio.instances.service.SingleInstanceService;
import com.bntech.imperio.instances.service.util.TypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class InstanceServiceImpl implements InstanceService {
    private final InstanceRepo instances;
    private final ErrorHandler errorHandler;
    private final SingleInstanceService singleInstances;


    @Autowired
    public InstanceServiceImpl(InstanceRepo instances, ErrorHandler errorHandler, SingleInstanceServiceImpl singleInstances) {
        this.instances = instances;
        this.errorHandler = errorHandler;
        this.singleInstances = singleInstances;
    }

    @Override
    public Mono<UserDetailsResponseDto> getInstanceDetails(Mono<String> id) {
        return id
                .transform(TypeConverter::monoStringToLong)
                .transform(instances::details)
                .transform(this::createDto);
    }

    @Override
    public Mono<ServerResponse> newDeployment(Mono<InstanceCreateRequest> instanceRequest) {
        return instanceRequest
                .flatMap(details -> switch (details.getRequestType()) {
                    //todo: There is a parse because we need diff requests for diff instances
                    case regular -> singleInstances.deploy(details);
                    case kubernetesHost -> singleInstances.deploy(details);
                    case kubernetesWorker -> singleInstances.deploy(details);
                });
    }


    private Mono<UserDetailsResponseDto> createDto(Mono<DatabaseInstanceDetailsDto> dto) {
        return dto.map(detailsDto -> new UserDetailsResponseDto(
                UserDetailsResponseDto.InstanceAlerts.builder()
                        .cpu(detailsDto.getI_alert_cpu())
                        .io(detailsDto.getI_alert_io())
                        .network_in(detailsDto.getI_alert_network_in())
                        .network_out(detailsDto.getI_alert_network_out())
                        .transfer_quota(detailsDto.getI_alert_transfer_quota())
                        .build(),
                UserDetailsResponseDto.InstanceBackups.builder()
                        .available(detailsDto.getInstance_backup_available())
                        .enabled(detailsDto.getInstance_backup_enabled())
                        .last_successful(detailsDto.getInstance_last_successful())
                        .schedule(UserDetailsResponseDto.InstanceBackupSchedule.builder()
                                .day(detailsDto.getInstance_backup_day())
                                .window(detailsDto.getInstance_backup_window()).build())
                        .build(),
                detailsDto.getInstance_created(),
                detailsDto.getInstance_group(),
                detailsDto.getInstance_host_uuid(),
                detailsDto.getInstance_hypervisor(),
                Long.parseLong(detailsDto.getInstance_id().toString()),
                detailsDto.getInstance_image(),
                detailsDto.getI_ip_v4(),
                detailsDto.getI_ip_v6(),
                detailsDto.getInstance_label(),
                UserDetailsResponseDto.InstanceSpecs.builder()
                        .disk(detailsDto.getI_spec_disk())
                        .memory(detailsDto.getI_spec_memory())
                        .transfer(detailsDto.getI_spec_transfer())
                        .vcpus(detailsDto.getI_spec_vcpu()).build(),
                detailsDto.getInstance_status(),
                detailsDto.getInstance_tags(),
                detailsDto.getInstance_type(),
                detailsDto.getInstance_updated(),
                detailsDto.getInstance_watchdog_enable()));
    }
}
