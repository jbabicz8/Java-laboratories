package com.project.controller;

import com.project.model.Zadanie;
import com.project.service.ZadanieService;
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
public class ZadanieHandler {

    private final ZadanieService zadanieService;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        Pageable pageable = pageable(request);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(zadanieService.getZadanie(pageable), Zadanie.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return zadanieService.getZadanie(id)
                .flatMap(zadanie -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(zadanie))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Zadanie.class)
                .doOnNext(zadanie -> zadanie.setZadanieId(null))
                .flatMap(zadanieService::setZadanie)
                .flatMap(zadanie -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(zadanie));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return request.bodyToMono(Zadanie.class)
                .doOnNext(zadanie -> zadanie.setZadanieId(id))
                .flatMap(zadanieService::setZadanie)
                .flatMap(zadanie -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(zadanie));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return zadanieService.deleteZadanie(id)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> count(ServerRequest request) {
        return zadanieService.getZadanieCount()
                .flatMap(count -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(count));
    }

    public Mono<ServerResponse> findByProjectId(ServerRequest request) {
        Integer projectId = Integer.valueOf(request.pathVariable("projectId"));
        Pageable pageable = pageable(request);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(zadanieService.getZadanieByProjectId(projectId, pageable), Zadanie.class);
    }

    public Mono<ServerResponse> countByProjectId(ServerRequest request) {
        Integer projectId = Integer.valueOf(request.pathVariable("projectId"));

        return zadanieService.getZadanieCountByProjectId(projectId)
                .flatMap(count -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(count));
    }

    public Mono<ServerResponse> findByName(ServerRequest request) {
        String name = request.queryParam("name").orElse("");
        Pageable pageable = pageable(request);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(zadanieService.findByNameContaining(name, pageable), Zadanie.class);
    }

    public Mono<ServerResponse> countByName(ServerRequest request) {
        String name = request.queryParam("name").orElse("");

        return zadanieService.countByNameContaining(name)
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
