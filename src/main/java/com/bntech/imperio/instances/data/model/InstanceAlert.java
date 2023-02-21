package com.bntech.imperio.instances.data.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table(name = "instance_alert")
@JsonAutoDetect
public final class InstanceAlert {
    @Column("i_alert_id")
    @Id
    private final Integer id;
    @Column("i_alert_cpu")
    private final Integer cpu;
    @Column("i_alert_io")
    private final Integer io;
    @Column("i_alert_network_in")
    private final Integer network_in;
    @Column("i_alert_network_out")
    private final Integer network_out;
    @Column("i_alert_transfer_quota")
    private final Integer transfer_quota;

    @JsonCreator
    public InstanceAlert(Integer id,
                         Integer cpu,
                         Integer io,
                         Integer network_in,
                         Integer network_out,
                         Integer transfer_quota) {
        this.id = id;
        this.cpu = cpu;
        this.io = io;
        this.network_in = network_in;
        this.network_out = network_out;
        this.transfer_quota = transfer_quota;
    }

    @Column("i_alert_id")
    @Id
    public Integer id() {
        return id;
    }

    @Column("i_alert_cpu")
    public Integer cpu() {
        return cpu;
    }

    @Column("i_alert_io")
    public Integer io() {
        return io;
    }

    @Column("i_alert_network_in")
    public Integer network_in() {
        return network_in;
    }

    @Column("i_alert_network_out")
    public Integer network_out() {
        return network_out;
    }

    @Column("i_alert_transfer_quota")
    public Integer transfer_quota() {
        return transfer_quota;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (InstanceAlert) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.cpu, that.cpu) &&
                Objects.equals(this.io, that.io) &&
                Objects.equals(this.network_in, that.network_in) &&
                Objects.equals(this.network_out, that.network_out) &&
                Objects.equals(this.transfer_quota, that.transfer_quota);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cpu, io, network_in, network_out, transfer_quota);
    }

    @Override
    public String toString() {
        return "InstanceAlert[" +
                "id=" + id + ", " +
                "cpu=" + cpu + ", " +
                "io=" + io + ", " +
                "network_in=" + network_in + ", " +
                "network_out=" + network_out + ", " +
                "transfer_quota=" + transfer_quota + ']';
    }
}
