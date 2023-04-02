package com.bntech.imperio.instances.data.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "region")
@JsonAutoDetect
public class Region {
    @Column("region_id")
    @Id
    private String regionId;
    @Column("linode_name")
    private String linodeName;
    @Column("full_name")
    private String fullName;
}
