package com.bntech.imperio.instances.data.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table(name = "instance_spec")
@JsonAutoDetect
@Builder
public final class InstanceSpec {
    @Column("i_spec_id")
    private final Integer id;
    @Column("i_spec_disk")
    private final Integer disk;
    @Column("i_spec_memory")
    private final Integer memory;
    @Column("i_spec_transfer")
    private final Integer transfer;
    @Column("i_spec_vcpu")
    private final Integer vcpus;

    @JsonCreator
    public InstanceSpec(Integer id,
                        Integer disk,
                        Integer memory,
                        Integer transfer,
                        Integer vcpus) {
        this.id = id;
        this.disk = disk;
        this.memory = memory;
        this.transfer = transfer;
        this.vcpus = vcpus;
    }

    @Column("i_spec_id")
    @Id
    public Integer id() {
        return id;
    }

    @Column("i_spec_disk")
    public Integer disk() {
        return disk;
    }

    @Column("i_spec_memory")
    public Integer memory() {
        return memory;
    }

    @Column("i_spec_transfer")
    public Integer transfer() {
        return transfer;
    }

    @Column("i_spec_vcpu")
    public Integer vcpus() {
        return vcpus;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (InstanceSpec) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.disk, that.disk) &&
                Objects.equals(this.memory, that.memory) &&
                Objects.equals(this.transfer, that.transfer) &&
                Objects.equals(this.vcpus, that.vcpus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, disk, memory, transfer, vcpus);
    }

    @Override
    public String toString() {
        return "InstanceSpec[" +
                "id=" + id + ", " +
                "disk=" + disk + ", " +
                "memory=" + memory + ", " +
                "transfer=" + transfer + ", " +
                "vcpus=" + vcpus + ']';
    }
}
