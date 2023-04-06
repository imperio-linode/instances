package com.bntech.imperio.instances.service;

import com.bntech.imperio.instances.data.dto.UserDetailsResponseDto;
import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public interface InstanceService {

    Mono<UserDetailsResponseDto> getInstanceDetails(Mono<String> id);

    Mono<ServerResponse> newDeployment(Mono<InstanceCreateRequest> requestMono);

    Mono<ServerResponse> updateInstances();
}
