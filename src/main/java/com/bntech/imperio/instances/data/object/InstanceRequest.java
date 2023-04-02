package com.bntech.imperio.instances.data.object;

import com.bntech.imperio.instances.data.object.types.InstanceRequestType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonAutoDetect
@Getter
@Setter
@NoArgsConstructor
public class InstanceRequest<T> {
    private String type;
    private String region;
    private String label;
    @JsonProperty("request_type")
    private InstanceRequestType requestType;

    public InstanceRequest(String type, String region, String label, InstanceRequestType requestType) {
        this.type = type;
        this.region = region;
        this.label = label;
        this.requestType = requestType;
    }

    public T requestDetails() {
        return (T) this;
    }
}
