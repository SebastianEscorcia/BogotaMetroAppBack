package com.sena.BogotaMetroApp.mapper;

import com.sena.BogotaMetroApp.presentation.dto.common.UsuarioInfoDTO;
import com.sena.BogotaMetroApp.presentation.dto.usuario.UsuarioResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponseDTO toDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setCorreo(usuario.getCorreo());

        if (usuario.getRol() != null) {
            dto.setRol(usuario.getRol().getNombre());
        }

        if (usuario.getDatosPersonales() != null) {
            dto.setNombreCompleto(usuario.getDatosPersonales().getNombreCompleto());

        }

        return dto;
    }

    public UsuarioInfoDTO toUsuarioInfo(Usuario usuario) {

        if (usuario == null) {
            return null;
        }

        UsuarioInfoDTO dto = new UsuarioInfoDTO();
        dto.setIdUsuario(usuario.getId());
        dto.setCorreo(usuario.getCorreo());

        if (usuario.getDatosPersonales() != null) {
            dto.setNombreCompleto(usuario.getDatosPersonales().getNombreCompleto());
            dto.setTelefono(usuario.getDatosPersonales().getTelefono());
            dto.setTipoDocumento(usuario.getDatosPersonales().getTipoDocumento());
            dto.setNumDocumento(usuario.getDatosPersonales().getNumDocumento());
            dto.setDireccion(usuario.getDatosPersonales().getDireccion());
            dto.setFechaNacimiento(usuario.getDatosPersonales().getFechaNacimiento());
        }

        return dto;
    }
}
