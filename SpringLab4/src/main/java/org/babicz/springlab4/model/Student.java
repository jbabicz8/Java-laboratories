package org.babicz.springlab4.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="student",
       indexes = {
        @Index(name="idx_nazwisko", columnList = "nazwisko", unique = false),
        @Index(name = "idx_nr_indeksu", columnList = "nr_indeksu", unique = true)
       }) //Indeksujemy kolumny, które są najczęściej wykorzystywane do wyszukiwania studentów
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   
    @Column(name = "student_id")
    private Integer studentId;

    @Column(nullable = false, length = 50)
    private String imie;

    @Column(nullable = false, length = 100)
    private String nazwisko;

    @Column(nullable = false, length = 20)
    private String nrIndeksu;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false)
    private Boolean stacjonarny;

    @ManyToMany(mappedBy = "studenci")
    @JsonIgnoreProperties({"studenci"})

    private Set<Projekt> projekty;
}
