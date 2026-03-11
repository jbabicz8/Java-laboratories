package org.babicz.springlab5.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.babicz.springlab5.model.Projekt;
import org.babicz.springlab5.model.Zadanie;
import org.babicz.springlab5.service.ProjektService;
import org.babicz.springlab5.service.ZadanieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.logging.log4j.util.Strings; 

import java.util.Optional;

@Controller
@AllArgsConstructor
public class ZadanieController {
    private final ZadanieService zadanieService;
    private final ProjektService projektService; 

    @GetMapping("/zadanieList")
    public String getZadanieList(@RequestParam(name = "nazwa", required = false) String nazwa,
                                 Model model, Pageable pageable) {
        Page<Zadanie> zadaniaPage;
        if (Strings.isNotBlank(nazwa)) {
            zadaniaPage = zadanieService.findByNazwaContaining(nazwa, pageable);
        } else {
            zadaniaPage = zadanieService.getZadania(pageable);
        }
        model.addAttribute("zadania", zadaniaPage);
        model.addAttribute("nazwa", nazwa);
        return "zadanieList";
    }

    
    @GetMapping("/zadanieSearchByNazwa")
    public String zadanieSearchByNazwa(@RequestParam(name = "nazwa", required = false) String nazwa, Model model, Pageable pageable) {
        return getZadanieList(nazwa, model, pageable);
    }


    @GetMapping("/zadanieEdit")
    public String showZadanieEditForm(@RequestParam(name = "zadanieId", required = false) Integer zadanieId,
                                      @RequestParam(name = "projektId", required = false) Integer projektIdParam, // Dla preselekcji z linku
                                      Model model) {
        Zadanie zadanie;
        if (zadanieId != null) {
            Optional<Zadanie> optionalZadanie = zadanieService.getZadanie(zadanieId);
            if (optionalZadanie.isPresent()) {
                zadanie = optionalZadanie.get();
            } else {
                model.addAttribute("notFoundError", "Nie znaleziono zadania o ID: " + zadanieId);
                zadanie = new Zadanie();
            }
        } else {
            zadanie = new Zadanie();
            if (projektIdParam != null) { 
                Optional<Projekt> projektOpt = projektService.getProjekt(projektIdParam);
                projektOpt.ifPresent(zadanie::setProjekt);
            }
        }
        model.addAttribute("zadanie", zadanie);
        model.addAttribute("wszystkieProjekty", projektService.getProjekty(Pageable.unpaged()).getContent());
        return "zadanieEdit";
    }

    @PostMapping("/zadanieEdit")
    public String saveOrUpdateZadanie(@ModelAttribute @Valid Zadanie zadanie, BindingResult bindingResult,
                                      @RequestParam(name = "projektId", required = false) Integer projektIdParam,
                                      Model model, RedirectAttributes redirectAttributes) {
        if (projektIdParam != null) {
            Optional<Projekt> projektOpt = projektService.getProjekt(projektIdParam);
            if (projektOpt.isPresent()) {
                zadanie.setProjekt(projektOpt.get());
            } else {
                bindingResult.rejectValue("projekt", "error.projekt", "Wybrany projekt nie istnieje.");
            }
        } else { 
            if (zadanie.getProjekt() != null && zadanie.getProjekt().getProjektId() == null) {
                zadanie.setProjekt(null); 
            }
        }


        if (bindingResult.hasErrors()) {
            model.addAttribute("zadanie", zadanie);
            model.addAttribute("wszystkieProjekty", projektService.getProjekty(Pageable.unpaged()).getContent());
            return "zadanieEdit";
        }

        try {
            zadanieService.setZadanie(zadanie);
            redirectAttributes.addFlashAttribute("message", "Zadanie zapisane pomyślnie.");
        } catch (Exception e) {
            model.addAttribute("zadanie", zadanie);
            model.addAttribute("wszystkieProjekty", projektService.getProjekty(Pageable.unpaged()).getContent());
            model.addAttribute("errorMessage", "Wystąpił błąd podczas zapisywania zadania: " + e.getMessage());
            return "zadanieEdit";
        }
        return "redirect:/zadanieList";
    }

    @PostMapping(value = "/zadanieEdit", params = "cancel")
    public String cancelZadanieEdit() {
        return "redirect:/zadanieList";
    }

    @PostMapping(value = "/zadanieEdit", params = "delete")
    public String deleteZadanieInForm(@ModelAttribute Zadanie zadanie, RedirectAttributes redirectAttributes) {
        if (zadanie.getZadanieId() != null) {
            try {
                zadanieService.deleteZadanie(zadanie.getZadanieId());
                redirectAttributes.addFlashAttribute("message", "Zadanie usunięte pomyślnie.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Wystąpił błąd podczas usuwania zadania: " + e.getMessage());
            }
        }
        return "redirect:/zadanieList";
    }
}