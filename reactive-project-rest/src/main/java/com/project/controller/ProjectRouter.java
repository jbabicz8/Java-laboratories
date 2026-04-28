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
public class ProjectRouter {

    @Bean
    public RouterFunction<ServerResponse> projectRoutes(ProjectHandler handler) {
        return route(GET("/projects"), handler::findAll)
                .andRoute(GET("/projects/page"), handler::findAllPaged)
                .andRoute(GET("/projects/count"), handler::count)
                .andRoute(GET("/projects/search"), handler::findByName)
                .andRoute(GET("/projects/search/count"), handler::countByName)
                .andRoute(GET("/projects/{id}"), handler::findById)
                .andRoute(POST("/projects"), handler::create)
                .andRoute(PUT("/projects/{id}"), handler::update)
                .andRoute(DELETE("/projects/{id}"), handler::delete)
                .andRoute(DELETE("/projects/{id}/with-tasks"), handler::deleteWithTasks)
                .andRoute(DELETE("/projects"), handler::deleteAll)
                .andRoute(POST("/projects/{projectId}/tasks/{taskId}"), handler::addTaskToProject)
                .andRoute(DELETE("/projects/{projectId}/tasks/{taskId}"), handler::removeTaskFromProject)
                .andRoute(POST("/projects/{projectId}/students/{studentId}"), handler::addStudentToProject)
                .andRoute(DELETE("/projects/{projectId}/students/{studentId}"), handler::removeStudentFromProject);
    }
}
