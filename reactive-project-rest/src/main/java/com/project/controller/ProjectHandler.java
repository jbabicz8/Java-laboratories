package com.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.project.service.ProjectService;
import com.project.model.Projekt;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProjectHandler {
    private final ProjectService projectService;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findAll(), Projekt.class);
    }

    public Mono<ServerResponse> findAllPaged(ServerRequest request) {
        Pageable pageable = pageable(request);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findAllBy(pageable), Projekt.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return projectService.findById(id)
                .flatMap(projekt -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(projekt))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findByName(ServerRequest request) {
        String name = request.queryParam("name").orElse("");

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNazwa(name), Projekt.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Projekt.class)
                .flatMap(projectService::create)
                .flatMap(projekt -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(projekt));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return request.bodyToMono(Projekt.class)
                .doOnNext(projekt -> projekt.setProjektId(id))
                .flatMap(projectService::update)
                .flatMap(projekt -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(projekt));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return projectService.delete(id)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> deleteWithTasks(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return projectService.deleteProjectWithTasks(id)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> deleteAll(ServerRequest request) {
        return projectService.deleteAll()
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> count(ServerRequest request) {
        return projectService.count()
                .flatMap(count -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(count));
    }

    public Mono<ServerResponse> countByName(ServerRequest request) {
        String name = request.queryParam("name").orElse("");

        return projectService.countByNazwa(name)
                .flatMap(count -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(count));
    }

    public Mono<ServerResponse> addTaskToProject(ServerRequest request) {
        Integer projectId = Integer.valueOf(request.pathVariable("projectId"));
        Integer taskId = Integer.valueOf(request.pathVariable("taskId"));

        return projectService.addZadanieToProjekt(projectId, taskId)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> removeTaskFromProject(ServerRequest request) {
        Integer projectId = Integer.valueOf(request.pathVariable("projectId"));
        Integer taskId = Integer.valueOf(request.pathVariable("taskId"));

        return projectService.removeZadanieFromProjekt(projectId, taskId)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> addStudentToProject(ServerRequest request) {
        Integer projectId = Integer.valueOf(request.pathVariable("projectId"));
        Integer studentId = Integer.valueOf(request.pathVariable("studentId"));

        return projectService.addStudentToProjekt(projectId, studentId)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> removeStudentFromProject(ServerRequest request) {
        Integer projectId = Integer.valueOf(request.pathVariable("projectId"));
        Integer studentId = Integer.valueOf(request.pathVariable("studentId"));

        return projectService.removeStudentFromProjekt(projectId, studentId)
                .then(ServerResponse.noContent().build());
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
