package org.babicz.springlab5.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.babicz.springlab5.model.Student;
import org.babicz.springlab5.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/studentList")
    public String getStudentList(@RequestParam(name = "nazwisko", required = false) String nazwisko,
                                 Model model, Pageable pageable) {
        Page<Student> studenciPage;
        if (nazwisko != null && !nazwisko.trim().isEmpty()) {
            studenciPage = studentService.findByNazwiskoStartsWithIgnoreCase(nazwisko, pageable);
        } else {
            studenciPage = studentService.getStudenci(pageable);
        }
        model.addAttribute("studenci", studenciPage);
        model.addAttribute("nazwisko", nazwisko); 
        return "studentList";
    }

    @GetMapping("/studentEdit")
    public String showStudentEditForm(@RequestParam(name = "studentId", required = false) Integer studentId, Model model) {
        Student student;
        if (studentId != null) {
            Optional<Student> optionalStudent = studentService.getStudent(studentId);
            if (optionalStudent.isPresent()) {
                student = optionalStudent.get();
            } else {
                model.addAttribute("notFoundError", "Nie znaleziono studenta o ID: " + studentId);
                student = new Student();
            }
        } else {
            student = new Student();
        }
        model.addAttribute("student", student);
        return "studentEdit";
    }

    @PostMapping("/studentEdit")
    public String saveOrUpdateStudent(@ModelAttribute @Valid Student student, BindingResult bindingResult,
                                      Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("student", student);
            return "studentEdit";
        }
        try {
            studentService.setStudent(student);
            redirectAttributes.addFlashAttribute("message", "Student zapisany pomyślnie.");
        } catch (Exception e) {
            model.addAttribute("student", student);
            model.addAttribute("errorMessage", "Wystąpił błąd podczas zapisywania studenta: " + e.getMessage());
            return "studentEdit";
        }
        return "redirect:/studentList";
    }

    @PostMapping(value = "/studentEdit", params = "cancel")
    public String cancelStudentEdit() {
        return "redirect:/studentList";
    }

    @PostMapping(value = "/studentEdit", params = "delete")
    public String deleteStudentInForm(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        if (student.getStudentId() != null) {
            try {
                studentService.deleteStudent(student.getStudentId());
                redirectAttributes.addFlashAttribute("message", "Student usunięty pomyślnie.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Wystąpił błąd podczas usuwania studenta: " + e.getMessage());
            }
        }
        return "redirect:/studentList";
    }
}