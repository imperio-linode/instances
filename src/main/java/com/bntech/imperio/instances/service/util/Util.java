package com.bntech.imperio.instances.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Slf4j
public class Util {

    public static Mono<ServerResponse> stringServerResponse(Mono<String> instanceDetails) {
        return instanceDetails
                .flatMap(userResponse -> {
                    log.info("stringServerResponse: {} ", userResponse);
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(userResponse), String.class);
                });
    }

}
