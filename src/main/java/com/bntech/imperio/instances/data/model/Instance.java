package com.bntech.imperio.instances.data.model;

import com.bntech.imperio.instances.data.object.types.InstanceStatus;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.List;

@Table(name = "instance")
@JsonAutoDetect
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instance implements Persistable<Long> {
    //todo: We should add @Id here but after performing https://stackoverflow.com/a/62427578.
    //todo; TOP PRIO. Either delete or add works due to it.
    @Column("instance_id")
    @Id
    private Long id;
    @Column("instance_region")
    private String region;
    @Column("instance_alert_id")
    private Integer alert;
    @Column("instance_address_id")
    private Integer address;
    @Column("instance_specs_id")
    private Integer spec;
    @Column("instance_backup_available")
    private Boolean available;
    @Column("instance_backup_enabled")
    private Boolean enabled;
    @Column("instance_backup_last_successful")
    private Instant last_successful;
    @Column("instance_backup_day")
    private Integer backup_day;
    @Column("instance_backup_window")
    private String window;
    @Column("instance_created")
    private String created;
    @Column("instance_group")
    private String group;
    @Column("instance_host_uuid")
    private String host_uuid;
    @Column("instance_hypervisor")
    private String hypervisor;
    @Column("instance_image")
    private String image;
    @Column("instance_label")
    private String label;
    @Column("instance_status")
    private InstanceStatus status;
    @Column("instance_tags")
    private List<String> tags;
    @Column("instance_type")
    private String type;
    @Column("instance_updated")
    private String updated;
    @Column("instance_watchdog_enable")
    private Boolean watchdog_enabled;
    @Transient
    private Boolean newInstance;

    @JsonCreator
    public Instance(Long id, String region, Integer alert, Integer address, Integer spec, Boolean available, Boolean enabled, Instant last_successful, Integer backup_day, String window, String created, String group, String host_uuid, String hypervisor, String image, String label, String status, List<String> tags, String type, String updated, Boolean watchdog_enabled) {
        this.id = id;
        this.region = region;
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
        this.status = InstanceStatus.valueOf(status.toUpperCase());
        this.tags = tags;
        this.type = type;
        this.updated = updated;
        this.watchdog_enabled = watchdog_enabled;
    }

    @Override
    public String toString() {
        return String.format("Instance: %s, %s, %s", this.id, this.label, this.image);
    }

    @Override
    @Transient
    public boolean isNew() {
        return this.newInstance || id == null;
    }

    public Instance newInstance() {
        this.newInstance = true;
        return this;
    }

}
