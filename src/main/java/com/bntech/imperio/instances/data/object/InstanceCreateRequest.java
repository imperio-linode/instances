package com.bntech.imperio.instances.data.object;

import com.bntech.imperio.instances.data.model.Instance;
import com.bntech.imperio.instances.data.object.types.InstanceRequestType;
import com.bntech.imperio.instances.data.object.types.InstanceStatus;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class InstanceCreateRequest extends InstanceRequest<InstanceCreateRequest> {
    private String image;
    private String group;
    @JsonAlias("authorized_keys")
    private List<String> authorizedKeys;
    @JsonAlias("root_pass")
    private String rootPass;
    @JsonAlias("request_type")
    private InstanceRequestType requestType;

    public Instance toInstance() {
        return Instance
                .builder()
                .region(getRegion())
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
                .status(InstanceStatus.PROVISIONING)
                .tags(List.of("", ""))
                .type(getType())
                .updated(Instant.now().toString())
                .watchdog_enabled(false)
                .build().newInstance();
    }
}
