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
                .log("instances.handler.InstanceHandler.1")
                .transform(instanceService::receiveNewInstanceRequest)
                .transform(instanceService::linodeServicesDeploySingleEngine)
                .log("instances.handler.InstanceHandler.2");
    }

    private Mono<ServerResponse> serverResponse(Mono<?> responseBody) {
        return ServerResponse.status(200).contentType(MediaType.APPLICATION_JSON).body(responseBody, responseBody.getClass()).log();
    }

    private Mono<InstanceCreateRequest> bytesToObj(Mono<ByteBuf> buff) {
        return buff.map(b -> {
            log.info("BUFF TO ICR: {}", b.toString(US_ASCII));
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(b.toString(US_ASCII), InstanceCreateRequest.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).onErrorStop();
    }
}
