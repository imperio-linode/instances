package com.bntech.imperio.instances.data.model.repository;

import com.bntech.imperio.instances.data.model.InstanceRegion;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RegionRepo extends ReactiveCrudRepository<InstanceRegion, Integer> {

    @Query("select * from region")
    List<InstanceRegion> getAll();

    Mono<InstanceRegion> getRegionByRegionId(String regionId);
}
