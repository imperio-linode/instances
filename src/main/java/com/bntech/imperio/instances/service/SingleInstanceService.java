package com.bntech.imperio.instances.service;

import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface SingleInstanceService {
    Mono<ServerResponse> deploy(InstanceCreateRequest instanceDetails);

}
