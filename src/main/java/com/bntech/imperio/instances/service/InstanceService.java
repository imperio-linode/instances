package com.bntech.imperio.instances.service;

import com.bntech.imperio.instances.data.dto.UserDetailsResponseDto;
import com.bntech.imperio.instances.data.model.Instance;
import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface InstanceService {

    /**
     * Finds a deployed vm record in database
     * @param vmId: id of deployed instance
     * @return Mono<Instance>
     */
    Mono<Instance> getInstanceById(Mono<String> vmId);

    Mono<UserDetailsResponseDto> getInstanceDetails(Mono<String> id);

    Mono<Instance> receiveNewInstanceRequest(Mono<InstanceCreateRequest> requestMono);

    Flux<Instance> subscribeNewVmInfo(Flux<Instance> requestMono);

    Mono<ServerResponse> linodeServicesDeploySingleEngine(Mono<Instance> instance);
}
