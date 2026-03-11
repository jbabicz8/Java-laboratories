package org.babicz.springlab5.service;

import org.babicz.springlab5.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StudentService {
    Optional<Student> getStudent(Integer studentId);
    Student setStudent(Student student);
    void deleteStudent(Integer studentId);
    Page<Student> getStudenci(Pageable pageable);
    Optional<Student> getStudentByNrIndeksu(String nrIndeksu);
    Page<Student> findByNrIndeksuStartsWith(String nrIndeksu, Pageable pageable);
    Page<Student> findByNazwiskoStartsWithIgnoreCase(String nazwisko, Pageable pageable);
    Page<Student> getStudenciByProjektId(Integer projektId, Pageable pageable);
}