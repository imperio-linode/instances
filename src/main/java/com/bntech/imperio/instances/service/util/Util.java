package com.bntech.imperio.instances.service.util;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;


@Slf4j
public class Util {

    public static Mono<ServerResponse> stringServerResponse(Mono<String> instanceDetails) {
        return instanceDetails
                .flatMap(userResponse -> {
                    log.info("stringServerResponse: {} ", userResponse);

                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(userResponse), String.class);
                });
    }

    public static Mono<ServerResponse> stringServerResponse(String responseBody, HttpStatus status) {
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(responseBody));
    }

    public static Mono<ServerResponse> stringServerResponse(Tuple2<HttpResponseStatus, String> responseData) {
        HttpStatus status = HttpStatus.valueOf(responseData.getT1().code());
        String body = responseData.getT2();
        return stringServerResponse(body, status);
    }
}
