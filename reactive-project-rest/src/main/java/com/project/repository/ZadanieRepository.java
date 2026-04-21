package com.project.repository;

import com.project.model.Zadanie;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZadanieRepository extends ReactiveCrudRepository<Zadanie, Integer> {
}
