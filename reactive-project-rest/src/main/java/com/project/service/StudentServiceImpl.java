package com.project.service;

import com.project.model.Student;
import com.project.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Student> getStudent(Integer studentId) {
        return studentRepository.findById(studentId);
    }

    @Override
    public Mono<Student> update(Student student) {
        return studentRepository
                .findById(student.getStudentId())
                .flatMap(s -> {
                    s.setEmail(student.getEmail());
                    s.setImie(student.getImie());
                    s.setNazwisko(student.getNazwisko());
                    s.setStacjonarny(student.getStacjonarny());
                    s.setNrIndeksu(student.getNrIndeksu());
                    return studentRepository.save(s);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Student nie został znaleziony")))
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Student> create(Student student) {
        return studentRepository.save(student);
        // return r2dbcEntityTemplate.insert(Student.class).using(student);
    }

    @Override
    public Mono<Void> deleteStudent(Integer studentId) {
        return studentRepository.deleteById(studentId);
    }

    @Override
    public Flux<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Mono<Long> getStudentsCount() {
        return studentRepository.count();
    }

    @Override
    public Mono<Student> getStudentByIndexNumber(String indexNumber) {
        return studentRepository.findByNrIndeksu(indexNumber);
    }

    @Override
    public Flux<Student> findByIndexNumberStartsWith(String indexNumber, Pageable pageable) {
        return studentRepository.findByNrIndeksuStartsWith(indexNumber, pageable);
    }

    @Override
    public Mono<Long> countByIndexNumberStartsWith(String indexNumber) {
        return studentRepository.countByNrIndeksuStartsWith(indexNumber);
    }

    @Override
    public Flux<Student> findByLastNameStartsWithIgnoreCase(String lastName, Pageable pageable) {
        return studentRepository.findByNazwiskoStartsWithIgnoreCase(lastName, pageable);
    }

    @Override
    public Mono<Long> countByLastNameStartsWithIgnoreCase(String lastName) {
        return studentRepository.countByNazwiskoStartsWithIgnoreCase(lastName);
    }

    @Override
    public Flux<Student> getStudentsByProjectId(Integer projectId, Pageable pageable) {
        return studentRepository.findByProjektId(projectId, pageable);
    }

    @Override
    public Mono<Long> getStudentsCountByProjectId(Integer projectId) {
        return studentRepository.countByProjektId(projectId);
    }

    @Override
    public Mono<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
}
