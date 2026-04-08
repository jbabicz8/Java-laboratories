package org.babicz.springlab4.rest;

import org.babicz.springlab4.config.JwtAuthFilter;
import org.babicz.springlab4.config.JwtService;
import org.babicz.springlab4.config.SecurityWebConfig;
import org.babicz.springlab4.controller.ProjektRestController;
import org.babicz.springlab4.model.Projekt;
import org.babicz.springlab4.service.ProjektService;
import org.babicz.springlab4.validation.ValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProjektRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityWebConfig.class)
public class ProjektControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjektService projektService;

    @MockBean
    private ValidationService<Projekt> validationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    private final String apiPath = "/api/projekty";

    @Test
    void getProjekt_shouldReturnProjectJson() throws Exception {
        Projekt projekt = new Projekt();
        projekt.setProjektId(1);
        projekt.setNazwa("Lab 4");

        when(projektService.getProjekt(1)).thenReturn(Optional.of(projekt));

        mockMvc.perform(get(apiPath + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projektId").value(1))
                .andExpect(jsonPath("$.nazwa").value("Lab 4"));
    }

    @Test
    void createProjekt_shouldReturnCreatedStatus() throws Exception {
        Projekt saved = new Projekt();
        saved.setProjektId(100);

        doNothing().when(validationService).validate(any(Projekt.class));
        when(projektService.setProjekt(any(Projekt.class))).thenReturn(saved);

        mockMvc.perform(post(apiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nazwa\":\"Nowy Projekt\", \"opis\":\"Opis\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void getProjekt_shouldReturnNotFound_whenProjectDoesNotExist() throws Exception {
        when(projektService.getProjekt(999)).thenReturn(Optional.empty());

        mockMvc.perform(get(apiPath + "/999"))
                .andExpect(status().isNotFound());
    }
}