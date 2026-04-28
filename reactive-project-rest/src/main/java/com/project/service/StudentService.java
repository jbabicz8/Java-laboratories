package com.project.service;
import com.project.model.Student;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentService {

    Mono<Student> getStudent(Integer studentId);

    Mono<Student> update(Student student);

    Mono<Student> create(Student student);



    Mono<Void> deleteStudent(Integer studentId);

    Flux<Student> getAllStudents();
    Mono<Long> getStudentsCount();

    Mono<Student> getStudentByIndexNumber(String indexNumber);

    Flux<Student> findByIndexNumberStartsWith(String indexNumber, Pageable pageable);
    Mono<Long> countByIndexNumberStartsWith(String indexNumber);

    Flux<Student> findByLastNameStartsWithIgnoreCase(String lastName, Pageable pageable);
    Mono<Long> countByLastNameStartsWithIgnoreCase(String lastName);

    Flux<Student> getStudentsByProjectId(Integer projectId, Pageable pageable);
    Mono<Long> getStudentsCountByProjectId(Integer projectId);

    Mono<Student> findByEmail(String email);

}
