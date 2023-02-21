package com.bntech.imperio.instances.service.impl;

import com.bntech.imperio.instances.data.dto.InstanceDetailsDto;
import com.bntech.imperio.instances.data.model.Instance;
import com.bntech.imperio.instances.data.dto.InstanceDto;
import com.bntech.imperio.instances.data.model.InstanceAddress;
import com.bntech.imperio.instances.data.model.InstanceAlert;
import com.bntech.imperio.instances.data.model.InstanceSpec;
import com.bntech.imperio.instances.data.model.repository.InstanceAddressRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceAlertRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceSpecRepo;
import com.bntech.imperio.instances.data.object.InstanceRequest;
import com.bntech.imperio.instances.handler.ErrorHandler;
import com.bntech.imperio.instances.service.InstanceService;
import com.bntech.imperio.instances.service.util.TypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;


@Component
@Slf4j
public class InstanceServiceImpl implements InstanceService {
    private final InstanceRepo instances;
    private final InstanceAlertRepo alerts;
    private final InstanceSpecRepo specs;
    private final InstanceAddressRepo addresses;
    final ErrorHandler errorHandler;

    @Autowired
    public InstanceServiceImpl(InstanceRepo instances, InstanceAlertRepo alerts, InstanceSpecRepo specs, InstanceAddressRepo addresses, ErrorHandler errorHandler) {
        this.instances = instances;
        this.alerts = alerts;
        this.specs = specs;
        this.addresses = addresses;
        this.errorHandler = errorHandler;
    }


    @Override
    public Mono<Instance> vm(Mono<String> id) {
        log.info("Instance Service: Find vm");
        return id
                .transform(TypeConverter::monoStringToLong)
                .transform(instances::getById)
                .doOnSuccess(i -> log.info("HERE: " + i.toString()));
    }

    @Override
    public Mono<InstanceDetailsDto> vmDetails(Mono<String> id) {
        log.info("Instance Service: Find vm");
        return id
                .transform(TypeConverter::monoStringToLong)
                .transform(instances::details)
                .doOnSuccess(i -> log.info("HERE: " + i.toString()));
    }

    @Override
    public Flux<Instance> makeVm(Flux<InstanceRequest> requestMono) {
        return null;
    }

    @Override
    public Flux<Instance> subscribeNewVmInfo(Flux<Instance> requestMono) {
        return null;
    }

    @Override
    public Mono<InstanceDto> instanceToResponse(Mono<Instance> instance) {
        return null;
    }


    private Mono<InstanceDto> createDto(Mono<Instance> instance) {
            return null;

//                        alerts(InstanceDto.InstanceAlerts.builder()
//                                .cpu(n.getT2().cpu())
//                                .io(n.getT2().io())
//                                .network_in(n.getT2().network_in())
//                                .network_out(n.getT2().network_out())
//                                .transfer_quota(n.getT2().transfer_quota())
//                                .build()
//                        )
//                        .backups(InstanceDto.InstanceBackups.builder()
//                                .available(n.getT1().getAvailable())
//                                .enabled(n.getT1().getEnabled())
//                                .last_successful(n.getT1().getLast_successful())
//                                .schedule(InstanceDto.InstanceBackupSchedule.builder()
//                                        .day(n.getT1().getBackup_day())
//                                        .window(n.getT1().getWindow()).build())
//                                .build())
//                        .created(n.getT1().getCreated())
//                        .group(n.getT1().getGroup())
//                        .host_uuid(n.getT1().getHost_uuid())
//                        .hypervisor(n.getT1().getHypervisor())
//                        .id(n.getT1().getId())
//                        .image(n.getT1().getImage())
//                        .ipv4(n.getT4().instanceIpv4())
//                        .ipv6(n.getT4().instanceIpv6())
//                        .label(n.getT1().getLabel())
//                        .specs(InstanceDto.InstanceSpecs.builder()
//                                .disk(n.getT3().disk())
//                                .memory(n.getT3().memory())
//                                .transfer(n.getT3().transfer())
//                                .vcpus(n.getT3().vcpus()).build())
//                        .status(n.getT1().getStatus())
//                        .tags(tagsToList(n.getT1()))
//                        .type(n.getT1().getType())
//                        .updated(n.getT1().getUpdated())
//                        .watchdog_enabled(n.getT1().getWatchdog_enabled())

    }

    private static List<String> tagsToList(Instance i) {
        return Arrays.stream(i.getTags()
                .replace("[", "")
                .replace("]", "")
                .split(",")).toList();
    }
}
