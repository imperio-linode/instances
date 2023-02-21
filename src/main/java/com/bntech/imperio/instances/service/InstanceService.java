package com.bntech.imperio.instances.service;

import com.bntech.imperio.instances.data.dto.InstanceDetailsDto;
import com.bntech.imperio.instances.data.dto.InstanceDto;
import com.bntech.imperio.instances.data.model.Instance;
import com.bntech.imperio.instances.data.object.InstanceRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface InstanceService {

    /**
     * Finds a deployed vm record in database
     * @param vmId: id of deployed instance
     * @return Mono<Instance>
     */
    Mono<Instance> vm(Mono<String> vmId);

    Mono<InstanceDetailsDto> vmDetails(Mono<String> id);

    Flux<Instance> makeVm(Flux<InstanceRequest> requestMono);

    //start sending updates to front
    Flux<Instance> subscribeNewVmInfo(Flux<Instance> requestMono);


    Mono<InstanceDto> instanceToResponse(Mono<Instance> instance);
}
