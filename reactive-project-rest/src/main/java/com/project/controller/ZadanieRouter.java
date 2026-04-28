package com.project.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ZadanieRouter {

    @Bean
    public RouterFunction<ServerResponse> zadanieRoutes(ZadanieHandler handler) {
        return route(GET("/tasks"), handler::findAll)
                .andRoute(GET("/tasks/count"), handler::count)
                .andRoute(GET("/tasks/{id}"), handler::findById)
                .andRoute(POST("/tasks"), handler::create)
                .andRoute(PUT("/tasks/{id}"), handler::update)
                .andRoute(DELETE("/tasks/{id}"), handler::delete)
                .andRoute(GET("/tasks/search"), handler::findByName)
                .andRoute(GET("/tasks/search/count"), handler::countByName)
                .andRoute(GET("/projects/{projectId}/tasks"), handler::findByProjectId)
                .andRoute(GET("/projects/{projectId}/tasks/count"), handler::countByProjectId);
    }
}
