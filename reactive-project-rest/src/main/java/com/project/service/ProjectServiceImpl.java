package com.project.service;

import com.project.repository.ProjectRepository;
import com.project.repository.ZadanieRepository;
import com.project.model.Projekt;
import com.project.model.Zadanie;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ZadanieRepository zadanieRepository;
    private final TransactionalOperator transactionalOperator;
    private final R2dbcEntityTemplate entityTemplate;

    @Override
    public Flux<Projekt> findAll() {
        return projectRepository.findAll()
                .delayElements(Duration.ofSeconds(2)); // TODO usunąć opóźnienie tylko do testu
    }

    @Override
    public Mono<Projekt> findById(Integer projektId) {
        return projectRepository.findById(projektId);
        // return r2dbcEntityTemplate.selectOne(query(where("projekt_id").is(projektId)), Projekt.class);
    }

    @Override
    public Flux<Projekt> findByNazwa(String nazwa) {
        return projectRepository.findByNazwaContains(nazwa);
    }

    @Override
    public Mono<Projekt> create(Projekt projekt) {
        return entityTemplate.insert(Projekt.class).using(projekt);
        // return projectRepository.save(projekt);
    }

    @Override
    public Mono<Projekt> update(Projekt projekt) {
        return projectRepository
                .findById(projekt.getProjektId())
                .flatMap(p -> {
                    p.setNazwa(projekt.getNazwa());
                    p.setOpis(projekt.getOpis());
                    p.setDataOddania(projekt.getDataOddania());
                    return projectRepository.save(p);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Projekt nie został znaleziony")))
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Void> addProjectWithTasks(Projekt projekt, List<Zadanie> zadanie) {
        return projectRepository
                .save(projekt)
                .flatMapMany(savedProjekt -> Flux.fromIterable(zadanie)
                        .doOnNext(z -> z.setProjektId(savedProjekt.getProjektId()))
                        .flatMap(zadanieRepository::save))
                .then()
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Void> delete(Integer projektId) {
        return projectRepository.deleteById(projektId);
    }

    @Override
    public Mono<Void> deleteProjectWithTasks(Integer projektId) {
        return entityTemplate.getDatabaseClient()
                .sql("DELETE FROM zadanie WHERE projekt_id = :id")
                .bind("id", projektId)
                .fetch().rowsUpdated()
                .then(entityTemplate.getDatabaseClient()
                        .sql("DELETE FROM projekt WHERE projekt_id = :id")
                        .bind("id", projektId)
                        .fetch().rowsUpdated())
                .then();
    }

    @Override
    public Mono<Void> deleteAll() {
        return projectRepository.deleteAll();
    }

    @Override
    public Flux<Projekt> findAllBy(Pageable pageable) {
        return null;
    }

    @Override
    public Mono<Long> count() {
        return null;
    }

    @Override
    public Mono<Long> countByNazwa(String nazwa) {
        return null;
    }

    @Override
    public Mono<Void> addZadanieToProjekt(Integer projektId, Integer zadanieId) {
        return null;
    }

    @Override
    public Mono<Void> removeZadanieFromProjekt(Integer projektId, Integer zadanieId) {
        return null;
    }

    @Override
    public Mono<Void> addStudentToProjekt(Integer projektId, Integer studentId) {
        return null;
    }

    @Override
    public Mono<Void> removeStudentFromProjekt(Integer projektId, Integer studentId) {
        return null;
    }
}
