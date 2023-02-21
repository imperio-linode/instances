package com.bntech.imperio.instances.data.model.repository;

import com.bntech.imperio.instances.data.model.InstanceAddress;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface InstanceAddressRepo extends ReactiveCrudRepository<InstanceAddress, Integer> {

    Mono<InstanceAddress> getById(Mono<Integer> id);

}
