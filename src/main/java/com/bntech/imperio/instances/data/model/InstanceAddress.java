package com.bntech.imperio.instances.data.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Objects;

@Table(name = "instance_address")
@JsonAutoDetect
public final class InstanceAddress {
    @Column("i_ip_id")
    private final Integer id;
    @Column("i_ip_v4")
    private final List<String> instanceIpv4;
    @Column("i_ip_v6")
    private final String instanceIpv6;

    @JsonCreator
    public InstanceAddress(Integer id, List<String> instanceIpv4, String instanceIpv6) {
        this.id = id;
        this.instanceIpv4 = instanceIpv4;
        this.instanceIpv6 = instanceIpv6;
    }

    @Column("i_ip_id")
    public Integer id() {
        return id;
    }

    @Column("i_ip_v4")
    public List<String> instanceIpv4() {
        return instanceIpv4;
    }

    @Column("i_ip_v6")
    public String instanceIpv6() {
        return instanceIpv6;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (InstanceAddress) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.instanceIpv4, that.instanceIpv4) &&
                Objects.equals(this.instanceIpv6, that.instanceIpv6);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, instanceIpv4, instanceIpv6);
    }

    @Override
    public String toString() {
        return "InstanceAddress[" +
                "id=" + id + ", " +
                "instanceIpv4=" + instanceIpv4 + ", " +
                "instanceIpv6=" + instanceIpv6 + ']';
    }
}
