package com.bntech.imperio.instances.data.object;

import com.bntech.imperio.instances.data.model.Instance;
import com.bntech.imperio.instances.data.object.types.InstanceRequestType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@JsonAutoDetect
@Getter
@Setter
@NoArgsConstructor
public class InstanceCreateRequest extends InstanceRequest<InstanceCreateRequest> {
    private String image;
    private String group;
    private List<String> authorizedKeys;
    private String rootPass;

    public InstanceCreateRequest(String type, String region, String label, InstanceRequestType requestType, String image, String group, List<String> authorizedKeys, String rootPass) {
        super(type, region, label, requestType);
        this.image = image;
        this.group = group;
        this.authorizedKeys = authorizedKeys;
        this.rootPass = rootPass;
    }

    public Instance toInstance() {
        Instance i = new Instance();
        i.setType(getType());
        i.setCreated(Instant.now().toString());
        i.setRegion(getRegion());
        i.setLabel(getLabel());
        i.setImage(getImage());
        i.setGroup(getGroup());
        i.setTags(List.of("", ""));

        return i;
    }
}
