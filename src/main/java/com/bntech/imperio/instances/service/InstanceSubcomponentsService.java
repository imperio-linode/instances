package com.bntech.imperio.instances.service;

import com.bntech.imperio.instances.data.dto.InstanceLinodeResponseDto;
import com.bntech.imperio.instances.data.model.InstanceAddress;
import com.bntech.imperio.instances.data.model.InstanceAlert;
import com.bntech.imperio.instances.data.model.InstanceSpec;
import com.bntech.imperio.instances.data.model.repository.InstanceAddressRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceAlertRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceSpecRepo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.bntech.imperio.instances.service.util.TypeConverter.stringListToInetList;


@Service
public interface InstanceSubcomponentsService {

    Mono<Tuple3<InstanceAlert, InstanceAddress, InstanceSpec>> createAll(InstanceLinodeResponseDto dto) throws UnknownHostException;

    static Mono<InstanceAlert> createNewAlert(InstanceLinodeResponseDto dto, InstanceAlertRepo alerts) {
        return alerts.save(InstanceAlert.builder()
                .cpu(dto.alerts().cpu())
                .io(dto.alerts().io())
                .network_in(dto.alerts().network_in())
                .network_out(dto.alerts().network_out())
                .transfer_quota(dto.alerts().transfer_quota())
                .build());
    }
    Mono<InstanceAlert> createNewAlert(InstanceLinodeResponseDto dto);

    static Mono<InstanceSpec> createNewSpec(InstanceLinodeResponseDto dto, InstanceSpecRepo specs) {
        return specs.save(InstanceSpec.builder()
                .vcpus(dto.specs().vcpus())
                .transfer(dto.specs().transfer())
                .memory(dto.specs().memory())
                .build());
    }
    Mono<InstanceSpec> createNewSpec(InstanceLinodeResponseDto dto);

    static Mono<InstanceAddress> createNewAddress(InstanceLinodeResponseDto dto, InstanceAddressRepo addresses) throws UnknownHostException {
        return addresses.save(InstanceAddress.builder()
                .instanceIpv6(InetAddress.getByName(dto.ipv6().split("/")[0]))
                .instanceIpv4(stringListToInetList(dto.ipv4()))
                .build());
    }
    Mono<InstanceAddress> createNewAddress(InstanceLinodeResponseDto dto) throws UnknownHostException;
}
