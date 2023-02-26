package com.bntech.imperio.instances.data.object;

import com.bntech.imperio.instances.data.dto.UserDetailsResponseDto;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@JsonAutoDetect
public record InstanceResponse(List<UserDetailsResponseDto> responseBody) {

    @JsonCreator
    public InstanceResponse(@JsonProperty("instances") final List<UserDetailsResponseDto> responseBody) {
        this.responseBody = responseBody;
    }
}
