package com.bntech.imperio.instances.data.object.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InstanceRequestType {
    regular("regular"),
    kubernetesHost("kubernetes-host"),
    kubernetesWorker("kubernetes-worker");

    private final String InstanceRequestType;
}
