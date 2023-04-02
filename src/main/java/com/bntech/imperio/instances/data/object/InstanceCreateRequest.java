package com.bntech.imperio.instances.data.object;

import com.bntech.imperio.instances.data.model.Instance;
import com.bntech.imperio.instances.data.object.types.InstanceRequestType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

import static com.bntech.imperio.instances.service.util.Util.commonRegions;

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
        return Instance
                .builder()
                .region(commonRegions(getRegion()))
                .alert(null)
                .address(null)
                .spec(null)
                .available(false)
                .enabled(false)
                .last_successful(null)
                .backup_day(null)
                .window("")
                .created(Instant.now().toString())
                .group(getGroup())
                .host_uuid("")
                .hypervisor("")
                .image(getImage())
                .label(getLabel())
                .status("Requesting")
                .tags(List.of("", ""))
                .type(getType())
                .updated(Instant.now().toString())
                .watchdog_enabled(false).build();
    }
}
