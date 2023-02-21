package com.bntech.imperio.instances.config;

import lombok.NoArgsConstructor;

/**
 * Application constants.
 * todo: This should is in common. Need to build it somehow into container.
 */
@NoArgsConstructor
public final class Constants {

    public static final String api_HELLO = "/hello";
    public static final String api_ID = "/one/{id}";
    public static final String api_ADD = "/add";

}
