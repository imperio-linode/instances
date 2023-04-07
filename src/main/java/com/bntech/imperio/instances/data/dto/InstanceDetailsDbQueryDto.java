package com.bntech.imperio.instances.data.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.net.InetAddress;
import java.time.Instant;
import java.util.List;

@Data
@JsonAutoDetect
@AllArgsConstructor
public class InstanceDetailsDbQueryDto {
    Integer instance_id;
    Integer instance_alert_id;
    Integer instance_address_id;
    Integer instance_specs_id;
    Boolean instance_backup_available;
    Boolean instance_backup_enabled;
    Instant instance_last_successful;
    Integer instance_backup_day;
    String instance_backup_window;
    String instance_created;
    String instance_group;
    String instance_host_uuid;
    String instance_hypervisor;
    String instance_image;
    String instance_label;
    String instance_status;
    List<String> instance_tags;
    String instance_type;
    String instance_updated;
    Boolean instance_watchdog_enable;
    Integer i_ip_id;
    List<InetAddress> i_ip_v4;
    InetAddress i_ip_v6;
    Integer i_alert_id;
    Integer i_alert_cpu;
    Integer i_alert_io;
    Integer i_alert_network_in;
    Integer i_alert_network_out;
    Integer i_alert_transfer_quota;
    Integer i_spec_id;
    Integer i_spec_disk;
    Integer i_spec_memory;
    Integer i_spec_transfer;
    Integer i_spec_vcpu;
    String instance_region;
}
