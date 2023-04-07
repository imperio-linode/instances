package com.bntech.imperio.instances.service;

import com.bntech.imperio.instances.data.dto.InstanceDetailsDbQueryDto;
import com.bntech.imperio.instances.data.dto.InstanceLinodeResponseDto;
import com.bntech.imperio.instances.data.model.*;
import com.bntech.imperio.instances.data.model.repository.InstanceAddressRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceAlertRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceSpecRepo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuple4;

import java.net.Inet6Address;
import java.net.UnknownHostException;


@Service
public interface InstanceSubcomponentsService {

    Mono<Tuple3<InstanceAlert, InstanceAddress, InstanceSpec>> createAll(InstanceLinodeResponseDto dto) throws UnknownHostException;

    Mono<Tuple4<Instance, InstanceAlert, InstanceAddress, InstanceSpec>> allAboutOneToSubcomponents(InstanceDetailsDbQueryDto instance);

    Mono<InstanceAlert> getAlertById(Mono<Integer> id);

    Mono<InstanceAddress> getAddressById(Mono<Integer> id);

    Mono<InstanceSpec> getSpecById(Mono<Integer> id);

    Mono<Instance> upsertUpdate(Tuple4<Instance, InstanceAlert, InstanceAddress, InstanceSpec> tuple);

    static Mono<InstanceAlert> createNewAlert(InstanceLinodeResponseDto dto, InstanceAlertRepo alerts) {
        return alerts.save(InstanceAlert.builder()
                .cpu(dto.alerts().cpu())
                .io(dto.alerts().io())
                .network_in(dto.alerts().network_in())
                .network_out(dto.alerts().network_out())
                .transfer_quota(dto.alerts().transfer_quota())
                .build());

    }

    static Mono<InstanceSpec> createNewSpec(InstanceLinodeResponseDto dto, InstanceSpecRepo specs) {
        return specs.save(InstanceSpec.builder()
                .vcpus(dto.specs().vcpus())
                .transfer(dto.specs().transfer())
                .memory(dto.specs().memory())
                .build());
    }

    static Mono<InstanceAddress> createNewAddress(InstanceLinodeResponseDto dto, InstanceAddressRepo addresses) throws UnknownHostException {
        return addresses.save(InstanceAddress.builder()
                .instanceIpv4(dto.ipv4())
                .instanceIpv6(Inet6Address.getByName(dto.ipv6().split("/")[0]))
                .build());


    }
}
