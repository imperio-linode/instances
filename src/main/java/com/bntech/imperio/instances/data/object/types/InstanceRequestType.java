package com.bntech.imperio.instances.data.object.types;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InstanceRequestType {
    regular("regular"),
    kubernetesHost("kubernetes-host"),
    kubernetesWorker("kubernetes-worker");

    @JsonValue
    private final String InstanceRequestType;
}
