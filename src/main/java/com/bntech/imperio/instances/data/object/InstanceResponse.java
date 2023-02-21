package com.bntech.imperio.instances.data.object;

import com.bntech.imperio.instances.data.model.Instance;
import com.bntech.imperio.instances.service.impl.InstanceResponseSerializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@JsonSerialize(using = InstanceResponseSerializer.class)
public record InstanceResponse(Instance instance) {

    @JsonCreator
    public InstanceResponse(@JsonProperty("instance") final Instance instance) {
        this.instance = instance;
    }
}
