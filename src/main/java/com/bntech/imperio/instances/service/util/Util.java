package com.bntech.imperio.instances.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class Util {


    public static Mono<ServerResponse> stringServerResponse(Mono<String> instanceDetails) {
        return instanceDetails
                .log("io.handler.GatewayHandler.stringToServerResponse")
                .flatMap(userResponse -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(userResponse), String.class));
    }

}
