package com.project.repository;

import com.project.model.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentRepository extends ReactiveCrudRepository<Student, Integer> {

    Mono<Student> findByNrIndeksu(String nrIndeksu);
    Mono<Student> findByEmail(String email);

    Flux<Student> findByNrIndeksuStartsWith(String nrIndeksu, Pageable pageable);

    Mono<Long> countByNrIndeksuStartsWith(String nrIndeksu);

    Flux<Student> findByNazwiskoStartsWithIgnoreCase(String nazwisko, Pageable pageable);

    Mono<Long> countByNazwiskoStartsWithIgnoreCase(String nazwisko);

    Flux<Student> findByProjektId(Integer projektId, Pageable pageable);

    Mono<Long> countByProjektId(Integer projektId);

    @Modifying
    @Query("UPDATE student SET projekt_id = :projektId WHERE student_id = :studentId")
    Mono<Integer> dodajStudentaDoProjektu(Integer projektId, Integer studentId);

    @Modifying
    @Query("UPDATE student SET projekt_id = NULL WHERE projekt_id = :projektId AND student_id = :studentId")
    Mono<Integer> usunStudentaZProjektu(Integer projektId, Integer studentId);
}
