package com.bntech.imperio.instances.data.object;

import com.bntech.imperio.instances.data.dto.InstanceLinodeResponseDto;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@JsonAutoDetect
public record InstanceResponse(List<InstanceLinodeResponseDto> responseBody) {

    @JsonCreator
    public InstanceResponse(@JsonProperty("instances") final List<InstanceLinodeResponseDto> responseBody) {
        this.responseBody = responseBody;
    }
}
