package com.bntech.imperio.instances.service.impl;

import com.bntech.imperio.instances.data.dto.DatabaseInstanceDetailsDto;
import com.bntech.imperio.instances.data.model.Instance;
import com.bntech.imperio.instances.data.dto.UserDetailsResponseDto;
import com.bntech.imperio.instances.data.model.repository.InstanceRepo;
import com.bntech.imperio.instances.data.object.InstanceCreateRequest;
import com.bntech.imperio.instances.data.object.InstanceRequest;
import com.bntech.imperio.instances.handler.ErrorHandler;
import com.bntech.imperio.instances.service.InstanceService;
import com.bntech.imperio.instances.service.util.TypeConverter;
import com.bntech.imperio.instances.service.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;

import static com.bntech.imperio.instances.config.Constants.api_TERRAFORM_SINGLE_INSTANCE;
import static io.netty.util.CharsetUtil.US_ASCII;


@Component
@Slf4j
public class InstanceServiceImpl implements InstanceService {
    private final InstanceRepo instances;
    final ErrorHandler errorHandler;
    private final HttpClient linodeServices;

    @Autowired
    public InstanceServiceImpl(InstanceRepo instances, ErrorHandler errorHandler, HttpClient tlsClient, @Value("${infrastructure.linode-services.host}") String instancesHost) {
        this.instances = instances;
        this.errorHandler = errorHandler;
        this.linodeServices = tlsClient.baseUrl(instancesHost);
    }

    @Override
    public Mono<Instance> getInstanceById(Mono<String> id) {
        return id
                .transform(TypeConverter::monoStringToLong)
                .transform(instances::getById);
    }

    @Override
    public Mono<UserDetailsResponseDto> getInstanceDetails(Mono<String> id) {
        return id
                .transform(TypeConverter::monoStringToLong)
                .transform(instances::details)
                .transform(this::createDto);
    }

    @Override
    public Mono<Instance> receiveNewInstanceRequest(Mono<InstanceCreateRequest> instanceRequest) {
        return instanceRequest.flatMap(instanceDetails -> {

            ObjectMapper mapper = new ObjectMapper();
            ByteBuf requestBody;

            try {
                requestBody = Unpooled.wrappedBuffer(mapper.writeValueAsBytes(instanceDetails));
                log.info("receiveNewInstanceRequest flatmap2: " + requestBody.toString(StandardCharsets.US_ASCII));
            } catch (JsonProcessingException e) {
                return Mono.error(new ServerWebInputException("Error serializing request body."));
            }

            return buildInstance(instanceDetails);
        });
    }

    @Override
    public Mono<ServerResponse> linodeServicesDeploySingleEngine(Mono<Instance> instance) {
        return instance.flatMap(details -> {
            ObjectMapper mapper = new ObjectMapper();
            ByteBuf requestBody;
            log.info("Linode request outgoing label: " + details.getLabel());

            try {
                requestBody = Unpooled.wrappedBuffer(mapper.writeValueAsBytes(details));
                log.info("Linode request outgoing body: " + requestBody.toString(US_ASCII));
            } catch (JsonProcessingException e) {
                return Mono.error(new ServerWebInputException("Error serializing request body."));
            }

            return linodeServices.post()
                    .uri(api_TERRAFORM_SINGLE_INSTANCE)
                    .send(Mono.just(requestBody))
                    .responseSingle((res, buf) -> Util.stringServerResponse(buf.asString()))
                    .log("service.impl.RequestsImpl.linodeServicesDeploySingleEngine")
                    .onErrorResume(ex -> {
                        if (ex instanceof ServerWebInputException) {
                            ServerWebInputException swie = (ServerWebInputException) ex;
                            return ServerResponse.badRequest().body(BodyInserters.fromValue(swie.getMessage()));
                        } else {
                            // Handle other exceptions as needed
                            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(BodyInserters.fromValue("An error occurred while processing the instance create request."));
                        }
                    });
        });
    }

    @Override
    public Flux<Instance> subscribeNewVmInfo(Flux<Instance> requestMono) {
        return null;
    }

    private Mono<Instance> buildInstance(InstanceRequest instanceDetails) {
        return switch (instanceDetails.getRequestType()) {
            //todo: There is a parse because we need diff requests for diff instances
            case regular -> createRegularInstance((InstanceCreateRequest) instanceDetails);
            case kubernetesHost -> createKubeHostInstance((InstanceCreateRequest) instanceDetails);
            case kubernetesWorker -> createKubeWorkerInstance((InstanceCreateRequest) instanceDetails);
        };
    }

    private Mono<Instance> createRegularInstance(InstanceCreateRequest details) {
        return instances
                .save(details.toInstance());
    }

    private Mono<Instance> createKubeHostInstance(InstanceCreateRequest details) {
        return null;
    }

    private Mono<Instance> createKubeWorkerInstance(InstanceCreateRequest details) {
        return null;
    }



    private Mono<UserDetailsResponseDto> createDto(Mono<DatabaseInstanceDetailsDto> dto) {
        return dto.map(detailsDto -> new UserDetailsResponseDto(
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
                detailsDto.getI_ip_v6(),
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
                detailsDto.getInstance_watchdog_enable()));
    }
}
