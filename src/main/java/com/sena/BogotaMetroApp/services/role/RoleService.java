package com.sena.BogotaMetroApp.services.role;

import com.sena.BogotaMetroApp.persistence.models.Role;
import com.sena.BogotaMetroApp.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role crearRole(String nombre) {
        // Validar si ya existe
        if (roleRepository.findByNombre(nombre).isPresent()) {
            throw new RuntimeException("El rol '" + nombre + "' ya existe");
        }
        Role role = new Role();
        role.setNombre(nombre);
        return roleRepository.save(role);
    }

    public List<Role> listarRoles() {
        return roleRepository.findAll();
    }
}
