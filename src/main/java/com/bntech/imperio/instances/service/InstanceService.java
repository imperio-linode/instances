package com.bntech.imperio.instances.service;

import com.bntech.imperio.instances.data.dto.InstanceDetailsDbQueryDto;
import com.bntech.imperio.instances.data.dto.InstanceLinodeResponseDto;
import com.bntech.imperio.instances.data.model.Instance;
import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface InstanceService {

    Mono<InstanceDetailsDbQueryDto> getInstanceDetails(Mono<String> id);

    Mono<ServerResponse> handleInstanceCreateRequest(Mono<InstanceCreateRequest> requestMono);

    Flux<Instance> upsertLinodeData(List<InstanceLinodeResponseDto> dto);

    Mono<InstanceLinodeResponseDto> queryToResponse(Mono<InstanceDetailsDbQueryDto> dto);

}
