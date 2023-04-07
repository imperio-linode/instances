package com.bntech.imperio.instances.data.dto;

import com.bntech.imperio.instances.data.model.InstanceAlert;
import com.bntech.imperio.instances.data.model.InstanceSpec;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

import java.net.InetAddress;
import java.time.Instant;
import java.util.List;

@Builder
@JsonAutoDetect
public record InstanceLinodeResponseDto(
        InstanceLinodeReplyAlertDto alerts,
        InstanceLinodeReplyBackupDto backups,
        String created,
        String group,
        String host_uuid,
        String hypervisor,
        Long id,
        String image,
        List<InetAddress> ipv4,
        String ipv6,
        String label,
        InstanceLinodeReplySpecsDto specs,
        String status,
        List<String> tags,
        String type,
        String updated,
        Boolean watchdog_enabled,
        String region
) {

    @Builder
    @JsonAutoDetect
    public record InstanceLinodeReplyAlertDto(
            Integer cpu,
            Integer io,
            Integer network_in,
            Integer network_out,
            Integer transfer_quota
    ) {
    }

    @Builder
    @JsonAutoDetect
    public record InstanceLinodeReplyBackupDto(
            Boolean available,
            Boolean enabled,
            Instant last_successful,
            InstanceLinodeReplyBackupScheduleDto schedule
    ) {
    }

    @Builder
    @JsonAutoDetect
    public record InstanceLinodeReplyBackupScheduleDto(
            Integer day,
            String window
    ) {
    }

    @Builder
    @JsonAutoDetect
    public record InstanceLinodeReplySpecsDto(
            Integer disk,
            Integer memory,
            Integer vcpus,
            Integer gpus,
            Integer transfer
    ) {
    }
}

