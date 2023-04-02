package com.bntech.imperio.instances.service.impl;

import com.bntech.imperio.instances.data.model.Instance;
import com.bntech.imperio.instances.data.model.repository.InstanceRepo;
import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import com.bntech.imperio.instances.service.SingleInstanceService;
import com.bntech.imperio.instances.service.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpHeaderNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import static com.bntech.imperio.instances.config.Constants.api_CREATE_ENGINE;
import static io.netty.util.CharsetUtil.US_ASCII;

@Component
@Slf4j
public class SingleInstanceServiceImpl implements SingleInstanceService {
    private final InstanceRepo instances;
    private final HttpClient linodeServices;
    private final ObjectMapper mapper;

    @Autowired
    public SingleInstanceServiceImpl(InstanceRepo instances, HttpClient tlsClient, @Value("${infrastructure.linode-services.host}") String instancesHost) {
        this.instances = instances;
        this.linodeServices = tlsClient.baseUrl(instancesHost);
        this.mapper = new ObjectMapper();
    }

    @Override
    public Mono<ServerResponse> deploy(InstanceCreateRequest instanceDetails) {
//        todo: add cache to keep/delete from db after failed request.
        return createRegularInstance(instanceDetails)
                .transform(this::linodeServicesDeploySingleEngine);
    }

    Mono<ServerResponse> linodeServicesDeploySingleEngine(Mono<Instance> instance) {
        return instance.flatMap(details -> {
            log.info("Linode request outgoing label: " + details.getLabel());

            try {
                log.info("instanceService req: {}", Unpooled.wrappedBuffer(mapper.writeValueAsBytes(details)).toString(US_ASCII));
                return linodeServices
                        .headers(headers -> headers.set(HttpHeaderNames.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                        .post()
                        .uri(api_CREATE_ENGINE)
                        .send(Mono.just(Unpooled.wrappedBuffer(mapper.writeValueAsBytes(details))))
                        .responseSingle((res, buf) -> buf
                                .map(buff -> {
                                    log.info("instanceService.inside req [ {} ][ {} ][ {} ][ {} ]", res.status(), res.fullPath(), res.uri(), res.method());
                                    return buff.toString(US_ASCII);
                                })
                        )
                        .log("service.impl.instanceService.linodeServicesDeploySingleEngine.1")
                        .transform(Util::stringServerResponse)
                        .onErrorResume(ex -> {
                            if (ex instanceof ServerWebInputException) {
                                ServerWebInputException swie = (ServerWebInputException) ex;
                                return ServerResponse.badRequest().body(BodyInserters.fromValue(swie.getMessage()));
                            } else {
                                return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue("An error occurred while processing the instance create request."));
                            }
                        });

            } catch (JsonProcessingException e) {
                return Mono.error(new ServerWebInputException("Error serializing request body."));
            }
        });
    }

    private Mono<Instance> createRegularInstance(InstanceCreateRequest details) {
        return instances
                .save(details.toInstance())
                .log("save.instanceRequest.toInstance");
    }

}
