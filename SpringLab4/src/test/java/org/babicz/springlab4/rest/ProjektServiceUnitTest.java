package org.babicz.springlab4.rest;

import org.babicz.springlab4.model.Projekt;
import org.babicz.springlab4.repository.ProjektRepository;
import org.babicz.springlab4.repository.ZadanieRepository;
import org.babicz.springlab4.service.ProjektServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjektServiceUnitTest {
    @Mock
    private ProjektRepository mockProjektRepository;
    @Mock
    private ZadanieRepository mockZadanieRepository;
    @InjectMocks
    private ProjektServiceImpl projektService;

    @Test
    void getProjekt_shouldReturnProject_whenExists() {
        // Given
        Integer id = 1;
        Projekt projekt = new Projekt();
        projekt.setProjektId(id);
        projekt.setNazwa("Testowy Projekt");
        when(mockProjektRepository.findById(id)).thenReturn(Optional.of(projekt));

        // When
        Optional<Projekt> result = projektService.getProjekt(id);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getNazwa()).isEqualTo("Testowy Projekt");
        verify(mockProjektRepository).findById(id);
    }

    @Test
    void setProjekt_shouldSaveProject() {
        // Given
        Projekt projekt = new Projekt(null, "Nowy", "Opis", LocalDate.now());
        when(mockProjektRepository.save(any(Projekt.class))).thenReturn(projekt);

        // When
        Projekt saved = projektService.setProjekt(projekt);

        // Then
        assertThat(saved).isNotNull();
        verify(mockProjektRepository).save(projekt);
    }

    @Test
    void deleteProjekt_shouldDeleteAssignmentsAndProject() {
        // Given
        Integer id = 10;
        when(mockZadanieRepository.findZadaniaProjektu(id)).thenReturn(java.util.List.of());

        // When
        projektService.deleteProjekt(id);

        // Then
        verify(mockZadanieRepository).deleteAll(any());
        verify(mockProjektRepository).deleteById(id);
    }
}