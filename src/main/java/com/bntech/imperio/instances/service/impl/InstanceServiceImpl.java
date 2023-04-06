package com.bntech.imperio.instances.service.impl;

import com.bntech.imperio.instances.data.dto.DatabaseInstanceDetailsDto;
import com.bntech.imperio.instances.data.dto.UserDetailsResponseDto;
import com.bntech.imperio.instances.data.model.Region;
import com.bntech.imperio.instances.data.model.repository.*;
import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import com.bntech.imperio.instances.data.object.external.LinodeInstanceResponse;
import com.bntech.imperio.instances.exception.InstanceUpsertException;
import com.bntech.imperio.instances.handler.ErrorHandler;
import com.bntech.imperio.instances.service.InstanceService;
import com.bntech.imperio.instances.service.SingleInstanceService;
import com.bntech.imperio.instances.service.util.TypeConverter;
import com.bntech.imperio.instances.service.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpHeaderNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;


import static com.bntech.imperio.instances.config.Constants.api_LINODE_INSTANCE;
import static io.netty.util.CharsetUtil.US_ASCII;


@Component
@Slf4j
public class InstanceServiceImpl implements InstanceService {
    private final InstanceRepo instances;
    private final AddressRepo addresses;
    private final InstanceAlertRepo alerts;
    private final InstanceSpecRepo specs;
    private final RegionRepo regions;
    private final ErrorHandler errorHandler;
    private final SingleInstanceService singleInstances;
    private final HttpClient linodeApi;
    private final String linodeToken;

    @Autowired
    public InstanceServiceImpl(InstanceRepo instances,
                               AddressRepo addresses, InstanceAlertRepo alerts, InstanceSpecRepo specs, RegionRepo regions, ErrorHandler errorHandler,
                               SingleInstanceServiceImpl singleInstances,
                               HttpClient httpClient,
                               @Value("${infrastructure.linode-api.host}") String linode,
                               @Value("${infrastructure.linode-api.token}") String linodeToken
    ) {
        this.instances = instances;
        this.addresses = addresses;
        this.alerts = alerts;
        this.specs = specs;
        this.regions = regions;
        this.errorHandler = errorHandler;
        this.linodeApi = httpClient.baseUrl(linode);
        this.singleInstances = singleInstances;
        this.linodeToken = linodeToken;
    }

    @Override
    public Mono<UserDetailsResponseDto> getInstanceDetails(Mono<String> id) {
        return id
                .transform(TypeConverter::monoStringToLong)
                .transform(instances::details)
                .transform(this::createDto);
    }

    @Override
    public Mono<ServerResponse> newDeployment(Mono<InstanceCreateRequest> instanceRequest) {
        return instanceRequest
                .flatMap(details -> switch (details.getRequestType()) {
                    //todo: There is a parse because we need diff requests for diff instances
                    case regular -> singleInstances.deploy(details);
                    case kubernetesHost -> singleInstances.deploy(details);
                    case kubernetesWorker -> singleInstances.deploy(details);
                });
    }

    private Mono<String> fetchInstances() {
        return linodeApi
                .headers(headers -> headers.set("Authorization", "Bearer " + linodeToken).add(HttpHeaderNames.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .get()
                .uri(api_LINODE_INSTANCE)
                .responseSingle((res, buf) -> buf
                        .map(buff -> {
                            log.info("instanceService.inside req [ {} ][ {} ][ {} ][ {} ]", res.status(), res.fullPath(), res.uri(), res.method());
                            return buff.toString(US_ASCII);
                        })
                );
    }

    private Mono<String> upsertInstances(Mono<String> instancesStr) {
        return instancesStr
                .flatMap(req -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        LinodeInstanceResponse res = mapper.readValue(req, LinodeInstanceResponse.class);
                        res.getData().forEach(i -> log.info(i.label()));


//                        instances.saveAll(res.getData());
                    } catch (JsonProcessingException e) {
                        return Mono.error(new InstanceUpsertException(e.getMessage()));
                    }
                    return instancesStr;
                });
    }

    @Override
    public Mono<ServerResponse> updateInstances() {
        return fetchInstances()
                .log()
                .transform(this::upsertInstances)
                .log()
                .transform(Util::stringServerResponse);
    }

    private Mono<UserDetailsResponseDto> createDto(Mono<DatabaseInstanceDetailsDto> dto) {
        return dto.flatMap(detailsDto -> {
            Mono<Region> regionMono = regions.getRegionByRegionId(detailsDto.getI_region_id());
            return regionMono.map(region -> new UserDetailsResponseDto(
                    UserDetailsResponseDto.InstanceAlerts.builder()
                            .cpu(detailsDto.getI_alert_cpu())
                            .io(detailsDto.getI_alert_io())
                            .network_in(detailsDto.getI_alert_network_in())
                            .network_out(detailsDto.getI_alert_network_out())
                            .transfer_quota(detailsDto.getI_alert_transfer_quota())
                            .build(),
                    UserDetailsResponseDto.InstanceBackups.builder()
                            .available(detailsDto.getInstance_backup_available())
                            .enabled(detailsDto.getInstance_backup_enabled())
                            .last_successful(detailsDto.getInstance_last_successful())
                            .schedule(UserDetailsResponseDto.InstanceBackupSchedule.builder()
                                    .day(detailsDto.getInstance_backup_day())
                                    .window(detailsDto.getInstance_backup_window()).build())
                            .build(),
                    detailsDto.getInstance_created(),
                    detailsDto.getInstance_group(),
                    detailsDto.getInstance_host_uuid(),
                    detailsDto.getInstance_hypervisor(),
                    Long.parseLong(detailsDto.getInstance_id().toString()),
                    detailsDto.getInstance_image(),
                    detailsDto.getI_ip_v4(),
                    detailsDto.getI_ip_v6().toString(),
                    detailsDto.getInstance_label(),
                    UserDetailsResponseDto.InstanceSpecs.builder()
                            .disk(detailsDto.getI_spec_disk())
                            .memory(detailsDto.getI_spec_memory())
                            .transfer(detailsDto.getI_spec_transfer())
                            .vcpus(detailsDto.getI_spec_vcpu()).build(),
                    detailsDto.getInstance_status(),
                    detailsDto.getInstance_tags(),
                    detailsDto.getInstance_type(),
                    detailsDto.getInstance_updated(),
                    detailsDto.getInstance_watchdog_enable(),
                    region.getLinodeName()));
        });
    }
}
