package com.bntech.imperio.instances.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface UpdateService {
    Mono<String> updateInstances();
}
