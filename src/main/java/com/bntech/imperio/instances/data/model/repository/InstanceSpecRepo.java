package com.bntech.imperio.instances.data.model.repository;

import com.bntech.imperio.instances.data.model.InstanceSpec;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface InstanceSpecRepo extends ReactiveCrudRepository<InstanceSpec, Integer> {

    Mono<InstanceSpec> getById(Mono<Integer> id);

}
