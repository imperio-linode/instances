package com.bntech.imperio.instances.data.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.DayOfWeek;
import java.time.Instant;

@Table(name = "instance")
@JsonAutoDetect
//@JsonSerialize(using = JsonSerializer.class)
@Getter
@Setter
public class Instance {
    @Column("instance_id")
    @Id
    private final Long id;
    @Column("instance_alert_id")
    private final Integer alert;
    @Column("instance_address_id")
    private final Integer address;
    @Column("instance_spec_id")
    private final Integer spec;
    @Column("instance_backup_available")
    private final Boolean available;
    @Column("instance_backup_enabled")
    private final Boolean enabled;
    @Column("instance_backup_last_successful")
    private final Instant last_successful;
    @Column("instance_backup_day")
    private final Integer backup_day;
    @Column("instance_backup_window")
    private final String window;
    @Column("instance_created")
    private final String created;
    @Column("instance_group")
    private final String group;
    @Column("instance_host_uuid")
    private final String host_uuid;
    @Column("instance_hypervisor")
    private final String hypervisor;
    @Column("instance_image")
    private final String image;
    @Column("instance_label")
    private final String label;
    @Column("instance_status")
    private final String status;
    @Column("instance_tags")
    private final String tags;
    @Column("instance_type")
    private final String type;
    @Column("instance_updated")
    private final String updated;
    @Column("instance_watchdog_enable")
    private final Boolean watchdog_enabled;


    @JsonCreator
    public Instance(Long id, Integer alert, Integer address, Integer spec, Boolean available, Boolean enabled, Instant last_successful, Integer backup_day, String window, String created, String group, String host_uuid, String hypervisor, String image, String label, String status, String tags, String type, String updated, Boolean watchdog_enabled) {
        this.id = id;
        this.alert = alert;
        this.address = address;
        this.spec = spec;
        this.available = available;
        this.enabled = enabled;
        this.last_successful = last_successful;
        this.backup_day = backup_day;
        this.window = window;
        this.created = created;
        this.group = group;
        this.host_uuid = host_uuid;
        this.hypervisor = hypervisor;
        this.image = image;
        this.label = label;
        this.status = status;
        this.tags = tags;
        this.type = type;
        this.updated = updated;
        this.watchdog_enabled = watchdog_enabled;
    }

    @Override
    public String toString() {
        return String.format("Instance: %s, %s, %s", this.id, this.label, this.image);
    }


}
