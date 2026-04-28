package com.project.controller;

import com.project.model.Student;
import com.project.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StudentHandler {

    private final StudentService studentService;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentService.getAllStudents(), Student.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return studentService.getStudent(id)
                .flatMap(student -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(student))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Student.class)
                .flatMap(studentService::create)
                .flatMap(student -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(student));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return request.bodyToMono(Student.class)
                .doOnNext(student -> student.setStudentId(id))
                .flatMap(studentService::update)
                .flatMap(student -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(student));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return studentService.deleteStudent(id)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> count(ServerRequest request) {
        return studentService.getStudentsCount()
                .flatMap(count -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(count));
    }

    public Mono<ServerResponse> findByIndexNumber(ServerRequest request) {
        String indexNumber = request.pathVariable("indexNumber");

        return studentService.getStudentByIndexNumber(indexNumber)
                .flatMap(student -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(student))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findByEmail(ServerRequest request) {
        String email = request.pathVariable("email");

        return studentService.findByEmail(email)
                .flatMap(student -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(student))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findByIndexNumberStartsWith(ServerRequest request) {
        String value = request.queryParam("value").orElse("");
        Pageable pageable = pageable(request);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentService.findByIndexNumberStartsWith(value, pageable), Student.class);
    }

    public Mono<ServerResponse> countByIndexNumberStartsWith(ServerRequest request) {
        String value = request.queryParam("value").orElse("");

        return studentService.countByIndexNumberStartsWith(value)
                .flatMap(count -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(count));
    }

    public Mono<ServerResponse> findByLastNameStartsWith(ServerRequest request) {
        String value = request.queryParam("value").orElse("");
        Pageable pageable = pageable(request);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentService.findByLastNameStartsWithIgnoreCase(value, pageable), Student.class);
    }

    public Mono<ServerResponse> countByLastNameStartsWith(ServerRequest request) {
        String value = request.queryParam("value").orElse("");

        return studentService.countByLastNameStartsWithIgnoreCase(value)
                .flatMap(count -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(count));
    }

    public Mono<ServerResponse> findByProjectId(ServerRequest request) {
        Integer projectId = Integer.valueOf(request.pathVariable("projectId"));
        Pageable pageable = pageable(request);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentService.getStudentsByProjectId(projectId, pageable), Student.class);
    }

    public Mono<ServerResponse> countByProjectId(ServerRequest request) {
        Integer projectId = Integer.valueOf(request.pathVariable("projectId"));

        return studentService.getStudentsCountByProjectId(projectId)
                .flatMap(count -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(count));
    }

    private Pageable pageable(ServerRequest request) {
        int page = request.queryParam("page")
                .map(Integer::parseInt)
                .orElse(0);

        int size = request.queryParam("size")
                .map(Integer::parseInt)
                .orElse(20);

        return PageRequest.of(page, size);
    }
}
