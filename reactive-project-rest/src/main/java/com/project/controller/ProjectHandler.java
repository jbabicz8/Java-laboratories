package com.project.controller;

import lombok.RequiredArgsConstructor;
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
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM) // odsyła kolejne wyniki widoczne jeżeli trwa ich dłużej
                // .contentType(MediaType.APPLICATION_JSON) poczeka na zebranie wszystkich wyników i wtedy odeśle
                .body(projectService.findAll(), Projekt.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        return projectService
                .findById(Integer.valueOf(request.pathVariable("id")))
                .flatMap(projekt -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(projekt))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
