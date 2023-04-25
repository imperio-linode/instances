package com.bntech.imperio.instances.service.util;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;
import reactor.netty.http.client.HttpClientResponse;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import static io.netty.util.CharsetUtil.US_ASCII;


@Slf4j
public class Util {

    public static Mono<ServerResponse> serverResponse(Mono<?> responseBody) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(responseBody, String.class);
    }

    @Deprecated
    private static Mono<ServerResponse> stringServerResponse(String responseBody, HttpStatus status) {
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(responseBody));
    }

    public static Mono<ServerResponse> serverResponse(Tuple2<HttpResponseStatus, String> responseData) {
        return stringServerResponse(responseData.getT2(), HttpStatus.valueOf(responseData.getT1().code()));
    }

    public static Mono<ServerResponse> externalStringResponse(HttpClientResponse res, ByteBufMono buf) {
        return buf.flatMap(buff -> {
            log.info("externalStringResponse: {} ", buff.toString(US_ASCII));
            return serverResponse(Tuples.of(res.status(), buff.toString(US_ASCII)));
        });
    }
}
