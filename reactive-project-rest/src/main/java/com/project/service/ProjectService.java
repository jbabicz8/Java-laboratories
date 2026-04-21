package com.project.service;

import com.project.model.Projekt;
import com.project.model.Zadanie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProjectService {

    Flux<Projekt> findAll();
    Mono<Projekt> findById(Integer projektId);
    Flux<Projekt> findByNazwa(String nazwa);
    Mono<Projekt> create(Projekt projekt);
    Mono<Projekt> update(Projekt projekt);
    Mono<Void> addProjectWithTasks(Projekt projekt, List<Zadanie> zadanie);
    Mono<Void> delete(Integer projektId);
    Mono<Void> deleteProjectWithTasks(Integer projektId);
    Mono<Void> deleteAll();

    Flux<Projekt> findAllBy(Pageable pageable);
    Mono<Long> count();
    Mono<Long> countByNazwa(String nazwa);

    Mono<Void> addZadanieToProjekt(Integer projektId, Integer zadanieId);
    Mono<Void> removeZadanieFromProjekt(Integer projektId, Integer zadanieId);
    Mono<Void> addStudentToProjekt(Integer projektId, Integer studentId);
    Mono<Void> removeStudentFromProjekt(Integer projektId, Integer studentId);

}
