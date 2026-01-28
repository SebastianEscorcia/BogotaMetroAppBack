package com.sena.BogotaMetroApp.services.role;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.models.Role;
import com.sena.BogotaMetroApp.persistence.repository.RoleRepository;
import com.sena.BogotaMetroApp.services.exception.rol.RolException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role crearRole(String nombre) {
        if (roleRepository.findByNombre(nombre).isPresent()) {
            throw new RolException(ErrorCodeEnum.ROL_YA_EXISTE);
        }
        Role role = new Role();
        role.setNombre(nombre);
        return roleRepository.save(role);
    }

    public Role updateRole(Role uRole, Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RolException(ErrorCodeEnum.ROL_NOT_FOUND));

        if (!role.getIsActive() && role.getNombre() == null)  throw new RolException( ErrorCodeEnum.ROL_DEACTIVATED);
        role.setNombre(uRole.getNombre());
        role.setIsActive(uRole.getIsActive());

        roleRepository.save(role);
        return role;
    }

    public void eliminarRole(Long id)  {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RolException(ErrorCodeEnum.ROL_NOT_FOUND));
        if (!role.isActive())  {
            throw new RolException(ErrorCodeEnum.ROL_DEACTIVATED);
        }

        role.setIsActive(false);
        roleRepository.save(role);
    }

    public List<Role> listarRoles() {
        return roleRepository.findAll();
    }
}
