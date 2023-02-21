//package com.bntech.imperio.instances.service.impl;
//
//import com.bntech.imperio.instances.data.object.InstanceResponse;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@Service
//@Slf4j
//public class InstanceSerializer extends JsonSerializer<InstanceResponse> {
//
//    @Override
//    public void serialize(InstanceResponse value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//        var i = value.instance();
//        log.info("SERIALIZING ");
//        gen.writeStartObject("instance_alerts");
//        gen.writeNumberField("cpu", i.alerts().cpu());
//        gen.writeNumberField("io", i.alerts().io());
//        gen.writeNumberField("network_in", i.alerts().network_in());
//        gen.writeNumberField("network_out", i.alerts().network_out());
//        gen.writeNumberField("transfer_quota", i.alerts().transfer_quota());
//        gen.writeEndObject();
//        gen.writeStartObject("instance_backups");
//        gen.writeBooleanField("available", i.backups().available());
//        gen.writeBooleanField("enabled", i.backups().enabled());
//        gen.writeStringField("last_successful", i.backups().last_successful().toString());
//        gen.writeStartObject("instance_schedule");
//        gen.writeStringField("day", i.backups().schedule().day().toString());
//        gen.writeStringField("window", i.backups().schedule().window());
//        gen.writeEndObject();
//        gen.writeStringField("created", i.created());
//        gen.writeStringField("group", i.group());
//        gen.writeStringField("host_uuid", i.host_uuid());
//        gen.writeStringField("hypervisor", i.hypervisor());
//        gen.writeNumberField("id", i.id());
//        gen.writeStringField("image", i.image());
//        gen.writeArrayFieldStart("ipv4");
//        gen.writeStartArray();
//        i.ipv4().forEach(s -> {
//            try {
//                gen.writeString(s);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        gen.writeEndArray();
//        gen.writeStringField("ipv6", i.ipv6());
//        gen.writeStringField("label", i.label());
//        gen.writeStartObject("spec");
//        gen.writeNumberField("disk", i.specs().disk());
//        gen.writeNumberField("memory", i.specs().memory());
//        gen.writeNumberField("transfer", i.specs().transfer());
//        gen.writeNumberField("vcpus", i.specs().vcpus());
//        gen.writeEndObject();
//        gen.writeStringField("status", i.status());
//        gen.writeArrayFieldStart("tags");
//        gen.writeStartArray();
//        i.tags().forEach(s -> {
//            try {
//                gen.writeString(s);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        gen.writeEndArray();
//        gen.writeStringField("type", i.type());
//        gen.writeStringField("updated", i.updated());
//        gen.writeBooleanField("watchdog_enabled", i.watchdog_enabled());
//        gen.writeEndObject();
//    }
//}
