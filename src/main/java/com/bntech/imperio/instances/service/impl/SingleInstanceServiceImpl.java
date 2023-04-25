package com.bntech.imperio.instances.service.impl;

import com.bntech.imperio.instances.data.model.repository.InstanceRepo;
import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import com.bntech.imperio.instances.service.SingleInstanceService;
import com.bntech.imperio.instances.service.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.function.Tuples;

import java.util.Objects;

import static com.bntech.imperio.instances.config.Constants.api_INSTANCE;

@Component
@Slf4j
public class SingleInstanceServiceImpl implements SingleInstanceService {
    private final InstanceRepo instances;
    private final HttpClient linodeServices;
    private final ObjectMapper mapper;
    private final TaskExecutor taskExecutor;

    @Autowired
    public SingleInstanceServiceImpl(InstanceRepo instances, HttpClient tlsClient, @Value("${infrastructure.linode-services.host}") String instancesHost, TaskExecutor taskExecutor) {
        this.instances = instances;
        this.taskExecutor = taskExecutor;
        this.linodeServices = tlsClient.baseUrl(instancesHost);
        this.mapper = new ObjectMapper();
    }

    @Override
    public Mono<ServerResponse> deploy(InstanceCreateRequest instanceDetails) {
//        todo: add cache to keep/delete from db after failed request.
        return linodeServicesDeploySingleEngine(Mono.just(instanceDetails))
                .onErrorResume(ex -> {
                    if (ex instanceof ServerWebInputException swie) {

                        return ServerResponse.status(swie.getStatusCode()).body(BodyInserters.fromValue(Objects.requireNonNull(swie.getReason())));
                    }

                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BodyInserters.fromValue(ex.getMessage()));
                });
    }

    @Override
    public Mono<ServerResponse> delete(Mono<Long> instanceId) {
        return instanceId.flatMap(id -> linodeServices.delete()
                .uri(api_INSTANCE + "?id=" + id)
                .responseSingle(Util::externalStringResponse)
                .onErrorResume(ex -> {
                    if (ex instanceof EmptyResultDataAccessException) {

                        return Util.serverResponse(Tuples.of(HttpResponseStatus.NOT_FOUND, "not found"));
                    } else {

                        return Util.serverResponse(Tuples.of(HttpResponseStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
                    }
                }));
    }


    Mono<ServerResponse> linodeServicesDeploySingleEngine(Mono<InstanceCreateRequest> instance) {
        return instance.flatMap(details -> Mono.fromCallable(() -> mapper.writeValueAsBytes(details))
                .flatMap(bytes -> linodeServices
                        .headers(headers -> headers.set(HttpHeaderNames.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                        .post()
                        .uri(api_INSTANCE)
                        .send(Mono.just(Unpooled.wrappedBuffer(bytes)))
                        .responseSingle(Util::externalStringResponse)
                        .flatMap(response -> {
                            if (response.statusCode() == HttpStatus.OK) {

                                return instances.save(details.toInstance()).then(Mono.just(response));
                            } else {

                                return Mono.just(response);
                            }
                        })
                        .onErrorResume(ServerWebInputException.class, swie -> ServerResponse
                                .status(swie.getStatusCode())
                                .body(BodyInserters.fromValue(Objects.requireNonNull(swie.getReason()))))
                        .onErrorResume(JsonProcessingException.class, e -> Mono.error(new RuntimeException("Error serializing request body.", e)))
                        .onErrorResume(ex -> ServerResponse
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(BodyInserters.fromValue("An error occurred while processing the instance create request.")))));
    }
}
