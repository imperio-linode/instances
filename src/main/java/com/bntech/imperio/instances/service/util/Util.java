package com.bntech.imperio.instances.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;


@Slf4j
public class Util {

    public static Mono<ServerResponse> stringServerResponse(Mono<String> instanceDetails) {
        return instanceDetails
                .log("linodeServicesDeploySingleEngine-stringServerResponse")
                .flatMap(userResponse -> {
                    log.info("strinServerResponse: {} ", userResponse);
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(userResponse), String.class);
                });
    }

    public static String commonRegions(String location) {
        Map<String, String> commonRegions = Map.of("us-east", "us-e", "us-west", "us-we");

        if (!commonRegions.get(location).isEmpty()) {
            return commonRegions.get(location);
        } else {
            //todo: repo search
            return commonRegions.get(location);
        }

    }
}
