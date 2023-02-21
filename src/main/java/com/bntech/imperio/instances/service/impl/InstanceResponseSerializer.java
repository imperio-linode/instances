package com.bntech.imperio.instances.service.impl;

import com.bntech.imperio.instances.data.object.InstanceResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class InstanceResponseSerializer extends JsonSerializer<InstanceResponse> {

    @Override
    public void serialize(InstanceResponse value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        var i = value.instance();
        gen.writeStartObject("instance");
        gen.writeNumberField("id", i.getId());
        gen.writeNumberField("alert_id", i.getAlert());
        gen.writeNumberField("address_id", i.getAddress());
        gen.writeNumberField("specs_id", i.getSpec());
        gen.writeBooleanField("backup_available", i.getAvailable());
        gen.writeBooleanField("backup_enabled", i.getEnabled());
        gen.writeStringField("backup_last_successful", i.getLast_successful().toString());
        gen.writeStringField("backup_day", i.getBackup_day().toString());
        gen.writeStringField("backup_window", i.getWindow());
        gen.writeStringField("created", i.getCreated());
        gen.writeStringField("group", i.getGroup());
        gen.writeStringField("host_uuid", i.getHost_uuid());
        gen.writeStringField("hypervisor", i.getHypervisor());
        gen.writeStringField("image", i.getImage());
        gen.writeStringField("label", i.getLabel());
        gen.writeStringField("status", i.getStatus());
        gen.writeStringField("tags", i.getTags());
        gen.writeStringField("type", i.getType());
        gen.writeStringField("updated", i.getUpdated());
        gen.writeBooleanField("watchdog_enable", i.getWatchdog_enabled());
        gen.writeEndObject();
    }
}
