package com.sena.BogotaMetroApp.mapper;

import com.sena.BogotaMetroApp.presentation.dto.usuario.UsuarioResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public UsuarioResponseDTO toDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setCorreo(usuario.getCorreo());
        dto.setRol(usuario.getRol().getNombre());

        if (usuario.getDatosPersonales() != null) {
            dto.setNombreCompleto(usuario.getDatosPersonales().getNombreCompleto());
        }

        return dto;
    }
}
