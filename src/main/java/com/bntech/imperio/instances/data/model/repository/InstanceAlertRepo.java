package com.bntech.imperio.instances.data.model.repository;

import com.bntech.imperio.instances.data.model.InstanceAlert;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface InstanceAlertRepo extends ReactiveCrudRepository<InstanceAlert, Integer> {

    Mono<InstanceAlert> getById(Mono<Integer> id);

}
