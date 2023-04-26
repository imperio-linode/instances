package com.bntech.imperio.instances.data.model.repository;

import com.bntech.imperio.instances.data.dto.InstanceDetailsDbQueryDto;
import com.bntech.imperio.instances.data.model.Instance;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface InstanceRepo extends ReactiveCrudRepository<Instance, Long> {

    @Query("select * from instance")
    List<Instance> getAll();

    @Query("select * from instance where instance_id = :id")
    Mono<Instance> getById(Mono<Long> id);

    @Query("select * from instance i inner join instance_address ad on i.instance_address_id = ad.i_ip_id inner join instance_alert al on i.instance_alert_id = al.i_alert_id inner join instance_spec s on i.instance_specs_id = s.i_spec_id where i.instance_id = :id;")
    Mono<InstanceDetailsDbQueryDto> allAboutOne(Mono<Long> id);

    @Modifying
    @Query("DELETE FROM instance WHERE instance_id NOT IN (:ids)")
    Mono<Void> deleteByNotInIds(List<Long> ids);
}
