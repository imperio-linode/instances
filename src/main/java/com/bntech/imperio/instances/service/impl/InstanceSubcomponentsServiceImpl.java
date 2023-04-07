package com.bntech.imperio.instances.service.impl;

import com.bntech.imperio.instances.data.dto.InstanceLinodeReplyDto;
import com.bntech.imperio.instances.data.model.InstanceAddress;
import com.bntech.imperio.instances.data.model.InstanceAlert;
import com.bntech.imperio.instances.data.model.InstanceRegion;
import com.bntech.imperio.instances.data.model.InstanceSpec;
import com.bntech.imperio.instances.data.model.repository.InstanceAddressRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceAlertRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceSpecRepo;
import com.bntech.imperio.instances.data.model.repository.RegionRepo;
import com.bntech.imperio.instances.service.InstanceSubcomponentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;

import java.net.UnknownHostException;

@Component
public class InstanceSubcomponentsServiceImpl implements InstanceSubcomponentsService {

    private final RegionRepo regions;
    private final InstanceAddressRepo addresses;
    private final InstanceAlertRepo alerts;
    private final InstanceSpecRepo specs;

    @Autowired
    public InstanceSubcomponentsServiceImpl(RegionRepo regions, InstanceAddressRepo addresses, InstanceAlertRepo alerts, InstanceSpecRepo specs) {
        this.regions = regions;
        this.addresses = addresses;
        this.alerts = alerts;
        this.specs = specs;
    }

    @Override
    public Mono<Tuple4<InstanceRegion, InstanceAlert, InstanceAddress, InstanceSpec>> createAll(InstanceLinodeReplyDto dto) throws UnknownHostException {
        return Mono.zip(
                InstanceSubcomponentsService.createNewRegion(dto, regions),
                InstanceSubcomponentsService.createNewAlert(dto, alerts),
                InstanceSubcomponentsService.createNewAddress(dto, addresses),
                InstanceSubcomponentsService.createNewSpec(dto, specs));
    }

    @Override
    public Mono<InstanceAlert> getAlertById(Mono<Integer> id) {
        return alerts.getById(id);
    }

    @Override
    public Mono<InstanceRegion> getRegionById(String id) {
        return regions.getRegionByRegionId(id);
    }

    @Override
    public Mono<InstanceAddress> getAddressById(Mono<Integer> id) {
        return addresses.getById(id);
    }

    @Override
    public Mono<InstanceSpec> getSpecById(Mono<Integer> id) {
        return specs.getById(id);
    }


}
