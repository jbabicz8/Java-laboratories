package org.babicz.springlab4.init;

import java.util.List;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.babicz.springlab4.model.Role;
import org.babicz.springlab4.repository.RoleRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DataInitializer {
    private final RoleRepository roleRepository;
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        List.of("USER", "ADMIN")
                .forEach(role -> roleRepository
                        .findByName(role)
                        .orElseGet(() -> roleRepository.save(Role.builder().name(role).build())));
    }
}