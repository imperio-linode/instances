package com.bntech.imperio.instances.data.model.repository;

import com.bntech.imperio.instances.data.model.InstanceAddress;
import com.bntech.imperio.instances.data.model.Region;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RegionRepo extends ReactiveCrudRepository<Region, Integer> {

    @Query("select * from region")
    List<Region> getAll();

    Mono<Region> getRegionByRegionId(String regionId);
}
