package com.bntech.imperio.instances.service;

import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public interface SingleInstanceService {
    Mono<ServerResponse> deploy(InstanceCreateRequest instanceDetails);
    Mono<ServerResponse> delete(Mono<Long> id);

}
