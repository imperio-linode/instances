package com.bntech.imperio.instances.service.impl;

import com.bntech.imperio.instances.data.object.external.LinodeInstanceResponse;
import com.bntech.imperio.instances.exception.InstanceUpsertException;
import com.bntech.imperio.instances.service.InstanceService;
import com.bntech.imperio.instances.service.UpdateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpHeaderNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import static com.bntech.imperio.instances.config.Constants.api_LINODE_INSTANCE;
import static io.netty.util.CharsetUtil.US_ASCII;

@Component
@Slf4j
public class UpdateServiceImpl implements UpdateService {
    private final HttpClient linodeApi;
    private final String linodeToken;
    private final InstanceService instanceService;

    @Autowired
    public UpdateServiceImpl(HttpClient httpClient,
                             @Value("${infrastructure.linode-api.host}") String linode,
                             @Value("${infrastructure.linode-api.token}") String linodeToken,
                             InstanceService instanceService) {
        this.linodeToken = linodeToken;
        this.instanceService = instanceService;
        this.linodeApi = httpClient.baseUrl(linode);
    }

    @Override
    public Mono<String> updateInstances() {
        return linodeApi
                .headers(headers -> headers.set("Authorization", "Bearer " + linodeToken).add(HttpHeaderNames.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .get()
                .uri(api_LINODE_INSTANCE)
                .responseSingle((res, buf) -> buf
                        .map(buff -> buff.toString(US_ASCII))
                )
                .flatMap(req -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        LinodeInstanceResponse requestBody = mapper.readValue(req, LinodeInstanceResponse.class);
                        //todo: need to remove instances that were not returned
                        return instanceService.upsertLinodeData(requestBody.getData())
                                .doOnNext(savedInstances -> log.info("Saved instances: {}", savedInstances))
                                .then(Mono.just(req));
                    } catch (JsonProcessingException e) {
                        return Mono.error(new InstanceUpsertException(e.getMessage()));
                    }
                });
    }
}
