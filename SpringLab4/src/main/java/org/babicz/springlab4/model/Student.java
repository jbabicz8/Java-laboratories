package org.babicz.springlab4.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="student",
       indexes = {
        @Index(name="idx_nazwisko", columnList = "nazwisko", unique = false),
               @Index(name = "idx_email", columnList = "email", unique = true),
               @Index(name = "idx_nr_indeksu", columnList = "nr_indeksu", unique = true)
       }) //Indeksujemy kolumny, które są najczęściej wykorzystywane do wyszukiwania studentów

public class Student implements UserDetails {
// STRONA 12 W INSTRUKCJI
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min=8, max=64, message="Hasło musi składać się z przynajmniej {min} i nie przekraczać {max} znaków")
    private String password;

    // dzięki FetchType.EAGER dane związane z tą relacją
    @ManyToMany(fetch = FetchType.EAGER) // będą natychmiast wczytywane, wraz z głównym obiektem tj. studentem
    @JoinTable(name = "student_role",
            joinColumns = {@JoinColumn(name="student_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles;

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

    @NotEmpty(message = "Nie podano adresu e-mail")
    @Email(message = "Niepoprawny format adresu e-mail")
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Boolean stacjonarny;

    @ManyToMany(mappedBy = "studenci")
    @JsonIgnoreProperties({"studenci"})

    private Set<Projekt> projekty;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }
    @Override
    public String getUsername() {
        return this.email; //zakładamy, że e-mail będzie wykorzystywany przy logowaniu
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
