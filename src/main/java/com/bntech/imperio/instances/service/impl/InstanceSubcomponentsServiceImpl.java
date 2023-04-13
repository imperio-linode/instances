package com.bntech.imperio.instances.service.impl;

import com.bntech.imperio.instances.data.dto.InstanceDetailsDbQueryDto;
import com.bntech.imperio.instances.data.dto.InstanceLinodeResponseDto;
import com.bntech.imperio.instances.data.model.*;
import com.bntech.imperio.instances.data.model.repository.InstanceAddressRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceAlertRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceSpecRepo;
import com.bntech.imperio.instances.service.InstanceSubcomponentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;

import java.net.UnknownHostException;


@Component
@Slf4j
public class InstanceSubcomponentsServiceImpl implements InstanceSubcomponentsService {

    private final InstanceAddressRepo addresses;
    private final InstanceAlertRepo alerts;
    private final InstanceSpecRepo specs;
    private final InstanceRepo instances;

    @Autowired
    public InstanceSubcomponentsServiceImpl(InstanceAddressRepo addresses, InstanceAlertRepo alerts, InstanceSpecRepo specs, InstanceRepo instances) {
        this.addresses = addresses;
        this.alerts = alerts;
        this.specs = specs;
        this.instances = instances;
    }

    @Override
    public Mono<Tuple3<InstanceAlert, InstanceAddress, InstanceSpec>> createAll(InstanceLinodeResponseDto dto) throws UnknownHostException {
        log.info("CreateAll: " + dto);
        return Mono.zip(
                createNewAlert(dto),
                createNewAddress(dto),
                createNewSpec(dto));
    }

    @Override
    public Mono<InstanceAlert> createNewAlert(InstanceLinodeResponseDto dto) {
        return InstanceSubcomponentsService.createNewAlert(dto, alerts);
    }

    @Override
    public Mono<InstanceAddress> createNewAddress(InstanceLinodeResponseDto dto) throws UnknownHostException {
        return InstanceSubcomponentsService.createNewAddress(dto, addresses);
    }

    @Override
    public Mono<InstanceSpec> createNewSpec(InstanceLinodeResponseDto dto) {
        return InstanceSubcomponentsService.createNewSpec(dto, specs);
    }

    //todo: Maybe in zip edit instance ids to match ones from zip.


    private InstanceAlert dtoToAlert(InstanceDetailsDbQueryDto instance) {
        return InstanceAlert.builder()
                .id(instance.getI_alert_id())
                .io(instance.getI_alert_io())
                .cpu(instance.getI_alert_cpu())
                .network_out(instance.getI_alert_network_out())
                .network_in(instance.getI_alert_network_in())
                .transfer_quota(instance.getI_alert_transfer_quota())
                .build();
    }

    private InstanceAddress dtoToAddress(InstanceDetailsDbQueryDto instance) {
        return InstanceAddress.builder()
                .id(instance.getI_ip_id())
                .instanceIpv4(instance.getI_ip_v4())
                .instanceIpv6(instance.getI_ip_v6())
                .build();
    }

    private InstanceSpec dtoToSpec(InstanceDetailsDbQueryDto instance) {
        return InstanceSpec.builder()
                .id(instance.getI_spec_id())
                .vcpus(instance.getI_spec_vcpu())
                .disk(instance.getI_spec_disk())
                .memory(instance.getI_spec_memory())
                .build();
    }

    private Instance dtoToInstance(InstanceDetailsDbQueryDto instance) {
        return Instance.builder()
                .id(instance.getInstance_id().longValue())
                .region(instance.getInstance_region())
                .alert(instance.getInstance_alert_id())
                .address(instance.getInstance_address_id())
                .spec(instance.getInstance_specs_id())
                .available(instance.getInstance_backup_available())
                .enabled(instance.getInstance_backup_enabled())
                .last_successful(instance.getInstance_last_successful())
                .backup_day(instance.getInstance_backup_day())
                .window(instance.getInstance_backup_window())
                .created(instance.getInstance_created())
                .group(instance.getInstance_group())
                .host_uuid(instance.getInstance_host_uuid())
                .hypervisor(instance.getInstance_hypervisor())
                .image(instance.getInstance_image())
                .label(instance.getInstance_label())
                .status(instance.getInstance_status())
                .tags(instance.getInstance_tags())
                .type(instance.getInstance_type())
                .updated(instance.getInstance_updated())
                .watchdog_enabled(instance.getInstance_watchdog_enable())
                .build();
    }
}

//    private Mono<Tuple4<Instance, InstanceAlert, InstanceAddress, InstanceSpec>> allAboutOneToSubcomponents(InstanceDetailsDbQueryDto instance) {
//        return Mono.zip(
//                Mono.just(dtoToInstance(instance)),
//                Mono.just(dtoToAlert(instance)),
//                Mono.just(dtoToAddress(instance)),
//                Mono.just(dtoToSpec(instance)));
//    }

//    private Mono<Instance> upsertInstanceSubscomponents(Tuple4<Instance, InstanceAlert, InstanceAddress, InstanceSpec> tuple) {
//        log.info("updating upsert: " + tuple.getT1().getId());
//        Mono<Instance> instance = instances.save(tuple.getT1());
//        Mono<InstanceAlert> alert = alerts.save(tuple.getT2());
//        Mono<InstanceAddress> address = addresses.save(tuple.getT3()).log("updateSubcomponentsAddress");
//        Mono<InstanceSpec> spec = specs.save(tuple.getT4());
//
//        return Mono.zip(instance, alert, address, spec)
//                .map(Tuple4::getT1)
//                .log();
//    }

//    @Override
//    public Mono<InstanceAlert> getAlertById(Mono<Integer> id) {
//        return alerts.getById(id);
//    }
//
//    @Override
//    public Mono<InstanceAddress> getAddressById(Mono<Integer> id) {
//        return addresses.getById(id);
//    }
//
//    @Override
//    public Mono<InstanceSpec> getSpecById(Mono<Integer> id) {
//        return specs.getById(id);
//    }
