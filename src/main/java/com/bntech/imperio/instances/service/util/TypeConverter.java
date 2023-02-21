package com.bntech.imperio.instances.service.util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class TypeConverter {

    public static Mono<Long> monoStringToLong(Mono<String> string) {
        return string.map(Long::parseLong);
    }

    public static Mono<Long> fluxStringToLong(Flux<String> string) {
        return string
                .map(Long::parseLong)
                .collectList()
                .transform(listMono -> listMono
                        .map(longs -> longs.get(0))
                );
    }
}
