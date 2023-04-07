package com.bntech.imperio.instances.data.object.external;

import com.bntech.imperio.instances.data.dto.InstanceLinodeReplyDto;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import java.util.List;

@JsonAutoDetect
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinodeInstanceResponse {
    private List<InstanceLinodeReplyDto> data;
    private Integer page;
    private Integer pages;
    private Integer results;
}
