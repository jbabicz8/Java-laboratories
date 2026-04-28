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
public class StudentRouter {

    @Bean
    public RouterFunction<ServerResponse> studentRoutes(StudentHandler handler) {
        return route(GET("/students"), handler::findAll)
                .andRoute(GET("/students/count"), handler::count)
                .andRoute(GET("/students/{id}"), handler::findById)
                .andRoute(POST("/students"), handler::create)
                .andRoute(PUT("/students/{id}"), handler::update)
                .andRoute(DELETE("/students/{id}"), handler::delete)
                .andRoute(GET("/students/index/{indexNumber}"), handler::findByIndexNumber)
                .andRoute(GET("/students/email/{email}"), handler::findByEmail)
                .andRoute(GET("/students/search/index"), handler::findByIndexNumberStartsWith)
                .andRoute(GET("/students/search/index/count"), handler::countByIndexNumberStartsWith)
                .andRoute(GET("/students/search/lastname"), handler::findByLastNameStartsWith)
                .andRoute(GET("/students/search/lastname/count"), handler::countByLastNameStartsWith)
                .andRoute(GET("/projects/{projectId}/students"), handler::findByProjectId)
                .andRoute(GET("/projects/{projectId}/students/count"), handler::countByProjectId);
    }
}
