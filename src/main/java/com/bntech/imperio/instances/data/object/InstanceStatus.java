package com.bntech.imperio.instances.data.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InstanceStatus {
    RUNNING("running"),
    OFFLINE("offline"),
    BOOTING("booting"),
    REBOOTING("rebooting"),
    SHUTTING_DOWN("shutting_down"),
    PROVISIONING("provisioning"),
    DELETING("deleting"),
    MIGRATING("migrating"),
    REBUILDING("rebuilding"),
    CLONING("cloning"),
    RESTORING("restoring"),
    STOPPED("stopped");

    private final String VmStatus;
}
