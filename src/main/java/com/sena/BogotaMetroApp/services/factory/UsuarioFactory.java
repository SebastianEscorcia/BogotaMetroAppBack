package com.sena.BogotaMetroApp.services.factory;

import com.sena.BogotaMetroApp.persistence.models.Role;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.RoleRepository;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.presentation.dto.usuario.RegistroUsuarioBaseDTO;
import com.sena.BogotaMetroApp.presentation.dto.usuario.UsuarioRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioFactory {

    private final BCryptPasswordEncoder encoder;
    private final RoleRepository roleRepository;
    public final UsuarioRepository usuarioRepository;

    public Usuario crearDesdeRegistro(RegistroUsuarioBaseDTO dto, String nombreRol) {
        validarCorreoUnico(nombreRol);

        Role rol = roleRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RuntimeException("El rol '" + nombreRol + "' no existe en el sistema"));

        Usuario usuario = new Usuario();
        usuario.setCorreo(dto.getCorreo());
        usuario.setClave(encoder.encode(dto.getClave()));
        usuario.setActivo(true);
        usuario.setRol(rol);

        return usuario;
    }

    public Usuario crearAdminDesdeRegistro(UsuarioRequestDTO dto, String nombreRol) {

        validarCorreoUnico(dto.getCorreo());

        Role rol = roleRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RuntimeException("El rol '" + nombreRol + "' no existe en el sistema"));

        Usuario usuario = new Usuario();
        usuario.setCorreo(dto.getCorreo());
        usuario.setClave(encoder.encode(dto.getClave()));
        usuario.setActivo(true);
        usuario.setRol(rol);

        return usuario;
    }

    private void validarCorreoUnico(String correo) {
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new RuntimeException("Ya existe un usuario registrado con el correo: " + correo);
        }
    }
}

