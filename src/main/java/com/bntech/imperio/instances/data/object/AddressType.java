package com.bntech.imperio.instances.data.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddressType {
    v4("ipv4"),
    v6("ipv6");

    private final String AddressType;
}
