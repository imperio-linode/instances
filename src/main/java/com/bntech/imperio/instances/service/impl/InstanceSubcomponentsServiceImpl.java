package com.bntech.imperio.instances.service.impl;

import com.bntech.imperio.instances.data.dto.InstanceLinodeReplyDto;
import com.bntech.imperio.instances.data.model.*;
import com.bntech.imperio.instances.data.model.repository.InstanceAddressRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceAlertRepo;
import com.bntech.imperio.instances.data.model.repository.InstanceSpecRepo;
import com.bntech.imperio.instances.data.model.repository.RegionRepo;
import com.bntech.imperio.instances.service.InstanceSubcomponentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuple5;

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
    public Mono<Tuple5<Instance, InstanceRegion, InstanceAlert, InstanceAddress, InstanceSpec>> fetchSubcomponents(Instance instance) {
        //todo: Crate CommonRepository for cross-object queries and put Instance Details and this one there.
        //Replace it so instead of 4 calls we are going to have 1. Other call is ok, needs to be moved to common repo.
        //Easier to debug like that on low data.
        return Mono.zip(
                Mono.just(instance),
                getRegionById(instance.getRegion()),
                getAlertById(Mono.just(instance.getAlert())),
                getAddressById(Mono.just(instance.getAddress())),
                getSpecById(Mono.just(instance.getSpec())));
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
