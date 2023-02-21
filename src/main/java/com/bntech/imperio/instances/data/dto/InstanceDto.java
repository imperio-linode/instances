package com.bntech.imperio.instances.data.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.List;

@Builder
@JsonAutoDetect
public record InstanceDto(
        InstanceAlerts alerts,
        InstanceBackups backups,
        String created,
        String group,
        String host_uuid,
        String hypervisor,
        Long id,
        String image,
        List<String> ipv4,
        String ipv6,
        String label,
        InstanceSpecs specs,
        String status,
        List<String> tags,
        String type,
        String updated,
        Boolean watchdog_enabled
) {

    public static InstanceDto toType (InstanceDto dto) {
        return dto;
    }

    @Builder
    @JsonAutoDetect
    public record InstanceAlerts(
            Integer cpu,
            Integer io,
            Integer network_in,
            Integer network_out,
            Integer transfer_quota
    ) {
    }

    @Builder
    @JsonAutoDetect
    public record InstanceBackups(
            Boolean available,
            Boolean enabled,
            Instant last_successful,
            InstanceBackupSchedule schedule
    ) {
    }

    @Builder
    @JsonAutoDetect
    public record InstanceBackupSchedule(
            Integer day,
            String window
    ) {
    }

    @Builder
    @JsonAutoDetect
    public record InstanceSpecs(
            Integer disk,
            Integer memory,
            Integer transfer,
            Integer vcpus
    ) {
    }
}

