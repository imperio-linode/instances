package com.bntech.imperio.instances.data.model.repository;

import com.bntech.imperio.instances.data.model.InstanceAddress;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.List;

public interface AddressRepo extends ReactiveCrudRepository<InstanceAddress, Integer> {

    @Query("select * from instance_address")
    List<InstanceAddress> getAll();

    @Query("select * from instance_address where i_ip_id = :ipId")
    List<InstanceAddress> getAllByIpId(Integer ipId);

}
