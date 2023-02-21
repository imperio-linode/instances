package com.bntech.imperio.instances.data.model.repository;

import com.bntech.imperio.instances.data.model.Instance;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface InstanceRepo extends ReactiveCrudRepository<Instance, Long> {
    @Query("select * from instance")
    List<Instance> getAll();

    @Query("select * from instance where instance_id = :id")
    Mono<Instance> getById(Mono<Long> id);
}
