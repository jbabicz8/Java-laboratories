package org.babicz.springlab5.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.babicz.springlab5.model.Projekt;
import org.babicz.springlab5.model.Student;
import org.babicz.springlab5.model.Zadanie;
import org.babicz.springlab5.service.ProjektService;
import org.babicz.springlab5.service.StudentService;
import org.babicz.springlab5.service.ZadanieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class ProjektController {
    private final ProjektService projektService;
    private final ZadanieService zadanieService;
    private final StudentService studentService;

    @GetMapping("/projektList")
    public String getProjektList(@RequestParam(name = "nazwa", required = false) String nazwa,
                                 Model model, Pageable pageable) {
        Page<Projekt> projektyPage;
        if (nazwa != null && !nazwa.trim().isEmpty()) {
            projektyPage = projektService.searchByNazwa(nazwa, pageable);
        } else {
            projektyPage = projektService.getProjekty(pageable);
        }

        Map<Integer, Map<String, Long>> statystyki = new HashMap<>();
        projektyPage.getContent().forEach(projekt -> {
            try {
                Map<String, Long> stats = new HashMap<>();
                stats.put("zadaniaCount", projektService.getZadaniaCount(projekt.getProjektId()));
                stats.put("studentCount", projektService.getStudentCount(projekt.getProjektId()));
                statystyki.put(projekt.getProjektId(), stats);
            } catch (Exception e) {
                statystyki.put(projekt.getProjektId(), Map.of("zadaniaCount", 0L, "studentCount", 0L));
            }
        });

        model.addAttribute("projekty", projektyPage);
        model.addAttribute("statystyki", statystyki);
        model.addAttribute("nazwa", nazwa); 
        return "projektList";
    }

    @GetMapping("/projektSearchByNazwa")
    public String projektSearchByNazwa(@RequestParam(name = "nazwa", required = false) String nazwa,
                                       Model model, Pageable pageable) {
        return getProjektList(nazwa, model, pageable);
    }

    @GetMapping("/projektEdit")
    public String showProjektEditForm(@RequestParam(name = "projektId", required = false) Integer projektId, Model model) {
        Projekt projekt;
        if (projektId != null) {
            Optional<Projekt> optionalProjekt = projektService.getProjekt(projektId);
            if (optionalProjekt.isPresent()) {
                projekt = optionalProjekt.get();
            } else {
                model.addAttribute("notFoundError", "Nie znaleziono projektu o ID: " + projektId);
                projekt = new Projekt(); 
            }
        } else {
            projekt = new Projekt();
        }
        model.addAttribute("projekt", projekt);
        return "projektEdit";
    }

    @PostMapping("/projektEdit")
    public String saveOrUpdateProjekt(@ModelAttribute @Valid Projekt projekt, BindingResult bindingResult,
                                      Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projekt", projekt); 
            return "projektEdit";
        }
        try {
            projektService.setProjekt(projekt);
            redirectAttributes.addFlashAttribute("message", "Projekt zapisany pomyślnie.");
        } catch (Exception e) {
            model.addAttribute("projekt", projekt);
            model.addAttribute("errorMessage", "Wystąpił błąd podczas zapisywania projektu: " + e.getMessage());
            return "projektEdit";
        }
        return "redirect:/projektList";
    }

    @PostMapping(value = "/projektEdit", params = "cancel")
    public String cancelProjektEdit() {
        return "redirect:/projektList";
    }

    @PostMapping(value = "/projektEdit", params = "delete")
    public String deleteProjektInForm(@ModelAttribute Projekt projekt, RedirectAttributes redirectAttributes) {
        if (projekt.getProjektId() != null) {
            try {
                projektService.deleteProjekt(projekt.getProjektId());
                redirectAttributes.addFlashAttribute("message", "Projekt usunięty pomyślnie.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Wystąpił błąd podczas usuwania projektu: " + e.getMessage());
            }
        }
        return "redirect:/projektList";
    }

    @GetMapping("/projektDetails")
    public String getProjektDetails(@RequestParam("projektId") Integer projektId, Model model, Pageable pageable) {
        Optional<Projekt> optionalProjekt = projektService.getProjekt(projektId);
        if (optionalProjekt.isEmpty()) {
            model.addAttribute("errorMessage", "Nie znaleziono projektu.");
            return "redirect:/projektList"; // lub dedykowana strona błędu
        }
        Projekt projekt = optionalProjekt.get();
        Page<Zadanie> zadaniaPage = zadanieService.getZadaniaByProjektId(projektId, pageable);
        Page<Student> studenciPage = studentService.getStudenciByProjektId(projektId, pageable);

        model.addAttribute("projekt", projekt);
        model.addAttribute("zadania", zadaniaPage.getContent());
        model.addAttribute("studenci", studenciPage.getContent());
        return "projektDetails";
    }

    @GetMapping("/projektAddZadanie")
    public String showProjektAddZadanieForm(@RequestParam("projektId") Integer projektId, Model model, Pageable pageable) {
        Optional<Projekt> optionalProjekt = projektService.getProjekt(projektId);
        if (optionalProjekt.isEmpty()) {
            model.addAttribute("errorMessage", "Nie znaleziono projektu.");
            return "redirect:/projektList";
        }
        model.addAttribute("projekt", optionalProjekt.get());
        Page<Zadanie> zadaniaPage = zadanieService.getZadania(Pageable.unpaged()); // Lub inna metoda serwisowa
        model.addAttribute("zadania", zadaniaPage.getContent());
        return "projektAddZadanie";
    }

    @PostMapping("/projektAddZadanie")
    public String addZadanieToProjekt(@RequestParam("projektId") Integer projektId,
                                      @RequestParam("zadanieId") Integer zadanieId,
                                      RedirectAttributes redirectAttributes) {
        try {
            projektService.addZadanieToProjekt(projektId, zadanieId);
            redirectAttributes.addFlashAttribute("message", "Zadanie dodane do projektu.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd podczas dodawania zadania: " + e.getMessage());
        }
        return "redirect:/projektDetails?projektId=" + projektId;
    }

    @GetMapping("/projektRemoveZadanie")
    public String removeZadanieFromProjekt(@RequestParam("projektId") Integer projektId,
                                           @RequestParam("zadanieId") Integer zadanieId,
                                           RedirectAttributes redirectAttributes) {
        try {
            projektService.removeZadanieFromProjekt(projektId, zadanieId);
            redirectAttributes.addFlashAttribute("message", "Zadanie usunięte z projektu.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd podczas usuwania zadania: " + e.getMessage());
        }
        return "redirect:/projektDetails?projektId=" + projektId;
    }

    @GetMapping("/projektAddStudent")
    public String showProjektAddStudentForm(@RequestParam("projektId") Integer projektId, Model model, Pageable pageable) {
        Optional<Projekt> optionalProjekt = projektService.getProjekt(projektId);
        if (optionalProjekt.isEmpty()) {
            model.addAttribute("errorMessage", "Nie znaleziono projektu.");
            return "redirect:/projektList";
        }
        model.addAttribute("projekt", optionalProjekt.get());
        Page<Student> studenciPage = studentService.getStudenci(Pageable.unpaged()); // Lub inna metoda serwisowa
        model.addAttribute("studenci", studenciPage.getContent());
        return "projektAddStudent";
    }

    @PostMapping("/projektAddStudent")
    public String addStudentToProjekt(@RequestParam("projektId") Integer projektId,
                                      @RequestParam("studentId") Integer studentId,
                                      RedirectAttributes redirectAttributes) {
        try {
            projektService.addStudentToProjekt(projektId, studentId);
            redirectAttributes.addFlashAttribute("message", "Student dodany do projektu.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd podczas dodawania studenta: " + e.getMessage());
        }
        return "redirect:/projektDetails?projektId=" + projektId;
    }

    @GetMapping("/projektRemoveStudent")
    public String removeStudentFromProjekt(@RequestParam("projektId") Integer projektId,
                                           @RequestParam("studentId") Integer studentId,
                                           RedirectAttributes redirectAttributes) {
        try {
            projektService.removeStudentFromProjekt(projektId, studentId);
            redirectAttributes.addFlashAttribute("message", "Student usunięty z projektu.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd podczas usuwania studenta: " + e.getMessage());
        }
        return "redirect:/projektDetails?projektId=" + projektId;
    }
}