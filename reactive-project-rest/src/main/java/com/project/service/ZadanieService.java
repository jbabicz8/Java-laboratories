package com.project.service;

import com.project.model.Zadanie;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ZadanieService {

    Mono<Zadanie> getZadanie(Integer zadanieId);

    Mono<Zadanie> setZadanie(Zadanie zadanie);

    Mono<Void> deleteZadanie(Integer zadanieId);

    Flux<Zadanie> getZadanie(Pageable pageable);

    Mono<Long> getZadanieCount();

    Flux<Zadanie> getZadanieByProjectId(Integer projectId, Pageable pageable);

    Mono<Long> getZadanieCountByProjectId(Integer projectId);

    Flux<Zadanie> findByNameContaining(String name, Pageable pageable);

    Mono<Long> countByNameContaining(String name);
}
