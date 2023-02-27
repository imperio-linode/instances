package com.bntech.imperio.instances.handler;


import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import com.bntech.imperio.instances.data.object.InstanceResponse;
import com.bntech.imperio.instances.service.InstanceService;
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
        return request.pathVariable("id")
                .transform(Mono::just)
                .transform(instanceService::getInstanceDetails)
                .map(dto -> new InstanceResponse(List.of(dto)))
                .log("instances.handler.InstanceHandler.instanceDetails")
                .transform(this::serverResponse)
                .onErrorResume(errorHandler::throwableError);
    }

    //todo: This should update db and send kafka message to linode-services that instance awaits creating. Can reply with ws after.
    public Mono<ServerResponse> addInstance(ServerRequest request) {
        return request.bodyToMono(ByteBuf.class)
                .transform(this::bytesToObj)
                .map(response -> {
                    log.info("Response in addInstance: " + response.toInstance().getLabel());
                    return response;
                })
                .transform(instanceService::receiveNewInstanceRequest);
    }

    private Mono<ServerResponse> serverResponse(Mono<?> mono) {
        return ServerResponse.status(200).contentType(MediaType.APPLICATION_JSON).body(mono, mono.getClass()).log();
    }

    private Mono<InstanceCreateRequest> bytesToObj(Mono<ByteBuf> buff) {
        return buff.map(b -> {
            log.info("instanceHandler byteToObj: {}", b.toString(US_ASCII));
            ObjectMapper mapper = new ObjectMapper();
            try {
                InstanceCreateRequest icr = mapper.readValue(b.toString(US_ASCII), InstanceCreateRequest.class);
                log.info("Mapped icr: {}", icr.getLabel());
                log.info("Mapped icr imnstance: {}", icr.toInstance().getLabel());
                return icr;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).onErrorStop();
    }
}
