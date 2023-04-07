package com.bntech.imperio.instances.service;

import com.bntech.imperio.instances.data.dto.InstanceLinodeReplyDto;
import com.bntech.imperio.instances.data.model.*;
import com.bntech.imperio.instances.data.model.repository.InstanceAddressRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceAlertRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceSpecRepo;
import com.bntech.imperio.instances.data.model.repository.RegionRepo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuple5;

import java.net.Inet6Address;
import java.net.UnknownHostException;

import static com.bntech.imperio.instances.service.util.Util.commonRegions;

@Service
public interface InstanceSubcomponentsService {

    Mono<Tuple4<InstanceRegion, InstanceAlert, InstanceAddress, InstanceSpec>> createAll(InstanceLinodeReplyDto dto) throws UnknownHostException;

    Mono<Tuple5<Instance, InstanceRegion, InstanceAlert, InstanceAddress, InstanceSpec>> fetchSubcomponents(Instance instance);

    Mono<InstanceAlert> getAlertById(Mono<Integer> id);
    Mono<InstanceRegion> getRegionById(String id);
    Mono<InstanceAddress> getAddressById(Mono<Integer> id);
    Mono<InstanceSpec> getSpecById(Mono<Integer> id);



    static Mono<InstanceAlert> createNewAlert(InstanceLinodeReplyDto dto, InstanceAlertRepo alerts) {
        InstanceAlert newAlert = InstanceAlert.builder()
                .cpu(dto.alerts().cpu())
                .io(dto.alerts().io())
                .network_in(dto.alerts().network_in())
                .network_out(dto.alerts().network_out())
                .transfer_quota(dto.alerts().transfer_quota())
                .build();

        return alerts.save(newAlert);
    }

    static Mono<InstanceRegion> createNewRegion(InstanceLinodeReplyDto dto, RegionRepo regions) {
        InstanceRegion newRegion = InstanceRegion.builder()
                .fullName(dto.region())
                .linodeName(commonRegions(dto.region()))
                .build();

        return regions.save(newRegion);
    }

    static Mono<InstanceSpec> createNewSpec(InstanceLinodeReplyDto dto, InstanceSpecRepo specs) {
        return specs.save(InstanceSpec.builder()
                .vcpus(dto.specs().vcpus())
                .transfer(dto.specs().transfer())
                .memory(dto.specs().memory())
                .build());
    }

    static Mono<InstanceAddress> createNewAddress(InstanceLinodeReplyDto dto, InstanceAddressRepo addresses) throws UnknownHostException {
        InstanceAddress newAddress =
                InstanceAddress.builder()
                        .instanceIpv4(dto.ipv4())
                        .instanceIpv6(Inet6Address.getByName(dto.ipv6()))
                        .build();

        return addresses.save(newAddress);
    }
}
