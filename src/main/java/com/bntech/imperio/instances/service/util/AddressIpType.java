package com.bntech.imperio.instances.service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddressIpType {
    v4("v4"),
    v6("v6");
    private final String IpType;
}
