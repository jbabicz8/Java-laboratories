package com.project.repository;

import com.project.model.Zadanie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ZadanieRepository extends ReactiveCrudRepository<Zadanie, Integer> {

    Flux<Zadanie> findAllBy(Pageable pageable);

    Flux<Zadanie> findByProjektId(Integer projektId);

    Flux<Zadanie> findByProjektId(Integer projektId, Pageable pageable);

    Mono<Long> countByProjektId(Integer projektId);

    Flux<Zadanie> findByNazwaContainingIgnoreCase(String nazwa, Pageable pageable);

    Mono<Long> countByNazwaContainingIgnoreCase(String nazwa);

    @Modifying
    @Query("DELETE FROM zadanie WHERE projekt_id = :projektId")
    Mono<Integer> deleteByProjektId(Integer projektId);
}
