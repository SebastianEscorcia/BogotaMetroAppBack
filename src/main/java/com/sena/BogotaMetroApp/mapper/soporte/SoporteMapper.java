package com.sena.BogotaMetroApp.mapper.soporte;

import com.sena.BogotaMetroApp.mapper.UsuarioMapper;
import com.sena.BogotaMetroApp.persistence.models.DatosPersonales;
import com.sena.BogotaMetroApp.persistence.models.Role;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.soporte.Soporte;
import com.sena.BogotaMetroApp.services.factory.DatosPersonalesFactory;
import com.sena.BogotaMetroApp.services.factory.UsuarioFactory;
import com.sena.BogotaMetroApp.utils.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SoporteMapper {

    private final UsuarioMapper usuarioMapper;

    private final UsuarioFactory usuarioFactory;
    private final DatosPersonalesFactory datosPersonalesFactory;

    public Soporte toEntity(SoporteRequestDTO dto,  Role rol) {

        Usuario usuario = usuarioFactory.crearDesdeRegistro(dto, RoleEnum.SOPORTE.toString());

        DatosPersonales dp = datosPersonalesFactory.crearDesdeRegistro(dto, usuario);
        usuario.setDatosPersonales(dp);

        Soporte soporte = new Soporte();
        soporte.setUsuario(usuario);
        soporte.setFechaCreacion(LocalDateTime.now());
        soporte.setUltimoAcceso(LocalDateTime.now());
        soporte.setId(usuario.getId());
        return soporte;


    }

    public SoporteResponseDTO toDTO(Soporte s) {
        SoporteResponseDTO dto = new SoporteResponseDTO();
        dto.setId(s.getId());
        dto.setCorreo(s.getUsuario().getCorreo());
        dto.setActivo(s.getUsuario().isActivo());
        dto.setFechaCreacion(s.getFechaCreacion());
        dto.setUltimoAcceso(s.getUltimoAcceso());

        dto.setUsuario(usuarioMapper.toUsuarioInfo(s.getUsuario()));

        return dto;
    }
}
