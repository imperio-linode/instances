package com.bntech.imperio.instances.handler;

import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import com.bntech.imperio.instances.data.object.InstanceResponse;
import com.bntech.imperio.instances.service.InstanceService;
import com.bntech.imperio.instances.service.UpdateService;
import com.bntech.imperio.instances.service.impl.UpdateServiceImpl;
import com.bntech.imperio.instances.service.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.netty.util.CharsetUtil.US_ASCII;


@Slf4j
@Component
public class InstanceHandler {

    final private InstanceService instanceService;
    final private ErrorHandler errorHandler;
    private final UpdateService updater;

    @Autowired
    public InstanceHandler(InstanceService instanceService, final ErrorHandler errorHandler, UpdateServiceImpl updater) {
        this.instanceService = instanceService;
        this.errorHandler = errorHandler;
        this.updater = updater;
    }

    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("Hello"));
    }

    public Mono<ServerResponse> details(ServerRequest request) {
        return request.pathVariable("id")
                .transform(Mono::just)
                .transform(instanceService::getInstanceDetails)
                .transform(instanceService::queryToResponse)
                .map(dto -> new InstanceResponse(List.of(dto)))
                .transform(this::jsonAndOk)
                .log()
                .onErrorResume(errorHandler::throwableError);
    }

    public Mono<ServerResponse> deploy(ServerRequest request) {
        return request.bodyToMono(ByteBuf.class)
                .transform(this::bytesToObj)
                .map(response -> {
                    log.info("Response in addInstance: " + response.getRootPass());

                    return response;
                })
                .transform(instanceService::handleInstanceCreateRequest);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        return updater.updateInstances()
                .transform(Util::stringServerResponse);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        return request.pathVariable("id")
                .transform(Mono::just)
                .transform(instanceService::deleteInstance)
                .transform(Util::stringServerResponse);
    }

    private Mono<ServerResponse> jsonAndOk(Mono<?> mono) {
        return ServerResponse.status(200).contentType(MediaType.APPLICATION_JSON).body(mono, mono.getClass());
    }

    private Mono<InstanceCreateRequest> bytesToObj(Mono<ByteBuf> buff) {
        return buff.<InstanceCreateRequest>handle((b, sink) -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                InstanceCreateRequest icr = mapper.readValue(b.toString(US_ASCII), InstanceCreateRequest.class);
                sink.next(icr);
            } catch (JsonProcessingException e) {
                sink.error(new RuntimeException(e));
            }
        }).onErrorStop();
    }
}
