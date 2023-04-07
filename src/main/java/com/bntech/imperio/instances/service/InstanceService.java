package com.bntech.imperio.instances.service;

import com.bntech.imperio.instances.data.dto.InstanceLinodeReplyDto;
import com.bntech.imperio.instances.data.model.Instance;
import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.UnknownHostException;
import java.util.List;

@Service
public interface InstanceService {

    Mono<InstanceLinodeReplyDto> getInstanceDetails(Mono<String> id);

    Mono<ServerResponse> newDeployment(Mono<InstanceCreateRequest> requestMono);

    Flux<Instance> upsertAll(List<InstanceLinodeReplyDto> dto);
    Mono<Instance> createNewInstance(InstanceLinodeReplyDto dto) throws UnknownHostException;
}
