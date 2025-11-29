package com.sena.BogotaMetroApp.services.factory;

import com.sena.BogotaMetroApp.persistence.models.Role;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.RoleRepository;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.RegistroPasajeroUnificadoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioFactory {

    private final BCryptPasswordEncoder encoder;
    private final RoleRepository roleRepository;

    public Usuario crearDesdeRegistro(RegistroPasajeroUnificadoDTO dto) {

        Usuario usuario = new Usuario();
        usuario.setCorreo(dto.getCorreo());
        usuario.setClave(encoder.encode(dto.getClave()));

        Role rolPasajero = roleRepository.findByNombre("PASAJERO").isPresent() ? roleRepository.findByNombre("PASAJERO").get() : null;
        if(rolPasajero == null){
            throw new RuntimeException("Rol de pasajero no existe");
        }

        usuario.setRol(rolPasajero);

        return usuario;
    }
}

