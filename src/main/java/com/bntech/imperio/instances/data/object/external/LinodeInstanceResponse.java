package com.bntech.imperio.instances.data.object.external;

import com.bntech.imperio.instances.data.dto.InstanceLinodeResponseDto;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonAutoDetect
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinodeInstanceResponse {
    private List<InstanceLinodeResponseDto> data;
    private Integer page;
    private Integer pages;
    private Integer results;
}
