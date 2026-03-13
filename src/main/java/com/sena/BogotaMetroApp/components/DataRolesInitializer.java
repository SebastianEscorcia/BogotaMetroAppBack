package com.sena.BogotaMetroApp.components;

import com.sena.BogotaMetroApp.persistence.models.Role;
import com.sena.BogotaMetroApp.persistence.repository.RoleRepository;
import com.sena.BogotaMetroApp.utils.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class DataRolesInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            for (RoleEnum role : RoleEnum.values()) {
                crearRolSiNoExiste(role.toString());
            }
        }
    }

    private void crearRolSiNoExiste(String nombreRol) {
        if (roleRepository.findByNombre(nombreRol).isEmpty()) {
            Role rol = new Role(nombreRol);
            rol.setNombre(nombreRol);
            roleRepository.save(rol);
        }
    }
}
