package com.project.repository;


import com.project.model.Projekt;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

@Repository
public interface ProjectRepository extends ReactiveCrudRepository<Projekt, Integer> {

    @Query("SELECT * FROM projekt nazwa ILIKE '%' || :nazwa || '%'")
    Flux<Projekt> findByNazwaContainingIgnoreCase(String nazwa, Pageable pageable);

    @Query("SELECT * FROM projekt nazwa ILIKE '%' || :nazwa || '%'") // lub $1
    Flux<Projekt> findByNazwaContains(String nazwa);

    @Query("UPDATE projekt SET data_oddania = :dataOddania")
    Mono<Integer> setFixedDeadLine(LocalDate dataOddania);

}
