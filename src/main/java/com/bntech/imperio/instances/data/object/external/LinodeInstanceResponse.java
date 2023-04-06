package com.bntech.imperio.instances.data.object.external;

import com.bntech.imperio.instances.data.dto.UserDetailsResponseDto;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import java.util.List;

@JsonAutoDetect
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinodeInstanceResponse {
    private List<UserDetailsResponseDto> data;
    private Integer page;
    private Integer pages;
    private Integer results;
}
