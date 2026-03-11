package org.babicz.springlab5.service;

import org.babicz.springlab5.model.Zadanie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ZadanieService {
    Optional<Zadanie> getZadanie(Integer zadanieId);
    Zadanie setZadanie(Zadanie zadanie);
    void deleteZadanie(Integer zadanieId);
    Page<Zadanie> getZadania(Pageable pageable);
    Page<Zadanie> getZadaniaByProjektId(Integer projektId, Pageable pageable);

    Page<Zadanie> findByNazwaContaining(String nazwa, Pageable pageable);

    
}