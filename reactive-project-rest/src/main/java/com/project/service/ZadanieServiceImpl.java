package com.project.service;

import com.project.model.Zadanie;
import com.project.repository.ZadanieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ZadanieServiceImpl implements ZadanieService {

    private final ZadanieRepository zadanieRepository;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Zadanie> getZadanie(Integer zadanieId) {
        return zadanieRepository.findById(zadanieId);
    }

    @Override
    public Mono<Zadanie> setZadanie(Zadanie zadanie) {
        if (zadanie.getZadanieId() == null) {
            return zadanieRepository.save(zadanie);
        }

        return zadanieRepository
                .findById(zadanie.getZadanieId())
                .flatMap(z -> {
                    z.setNazwa(zadanie.getNazwa());
                    z.setOpis(zadanie.getOpis());
                    z.setKolejnosc(zadanie.getKolejnosc());
                    z.setProjektId(zadanie.getProjektId());
                    return zadanieRepository.save(z);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Zadanie nie zostało znalezione")))
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Void> deleteZadanie(Integer zadanieId) {
        return zadanieRepository.deleteById(zadanieId);
    }

    @Override
    public Flux<Zadanie> getZadanie(Pageable pageable) {
        return zadanieRepository.findAllBy(pageable);
    }

    @Override
    public Mono<Long> getZadanieCount() {
        return zadanieRepository.count();
    }

    @Override
    public Flux<Zadanie> getZadanieByProjectId(Integer projectId, Pageable pageable) {
        return zadanieRepository.findByProjektId(projectId, pageable);
    }

    @Override
    public Mono<Long> getZadanieCountByProjectId(Integer projectId) {
        return zadanieRepository.countByProjektId(projectId);
    }

    @Override
    public Flux<Zadanie> findByNameContaining(String name, Pageable pageable) {
        return zadanieRepository.findByNazwaContainingIgnoreCase(name, pageable);
    }

    @Override
    public Mono<Long> countByNameContaining(String name) {
        return zadanieRepository.countByNazwaContainingIgnoreCase(name);
    }
}
