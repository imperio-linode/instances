package com.bntech.imperio.instances.data.object;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record InstanceRequest(
        String instanceName,
        String instanceSize,
        String region,
        String sshKey
) {
    @JsonCreator
    public InstanceRequest(@JsonProperty("userId") final String instanceName, final String instanceSize, final String region, final String sshKey) {
        this.instanceName = instanceName;
        this.instanceSize = instanceSize;
        this.region = region;
        this.sshKey = sshKey;
    }

}

