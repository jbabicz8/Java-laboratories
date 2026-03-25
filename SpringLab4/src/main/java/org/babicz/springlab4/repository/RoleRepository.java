package org.babicz.springlab4.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.babicz.springlab4.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}