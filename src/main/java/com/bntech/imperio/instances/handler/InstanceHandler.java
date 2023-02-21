package com.bntech.imperio.instances.handler;


import com.bntech.imperio.instances.data.dto.InstanceDetailsDto;
import com.bntech.imperio.instances.data.dto.InstanceDto;
import com.bntech.imperio.instances.data.model.Instance;
import com.bntech.imperio.instances.data.object.InstanceResponse;
import com.bntech.imperio.instances.service.InstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class InstanceHandler {

    final private InstanceService instanceService;
    final private ErrorHandler errorHandler;

    @Autowired
    public InstanceHandler(InstanceService instanceService, final ErrorHandler errorHandler) {
        this.instanceService = instanceService;
        this.errorHandler = errorHandler;
    }

    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("Hello"));
    }

    public Mono<ServerResponse> instanceDetails(ServerRequest request) {
        log.info("InstanceHandler path variable: {}", request.pathVariable("id"));

        return request.pathVariable("id")
                .transform(Mono::just)
                .log("com.bntech.long")
                .transform(instanceService::vmDetails)

//                .transform(instanceService::instanceToResponse)
                .log("com.bntech.instance")
                .transform(this::rawInstanceServerResponse);

    }

    public Mono<ServerResponse> addInstance(ServerRequest request) {
        return Mono.empty();
    }

    private Mono<ServerResponse> instanceServerResponse(Mono<InstanceResponse> instanceMono) {
        return ServerResponse.ok().body(instanceMono, InstanceResponse.class);
    }

    private Mono<ServerResponse> rawInstanceServerResponse(Mono<InstanceDetailsDto> instanceMono) {
        return ServerResponse.ok().body(instanceMono, InstanceDetailsDto.class);
    }

}
