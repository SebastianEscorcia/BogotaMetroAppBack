package com.sena.BogotaMetroApp.mapper.soporte;

import com.sena.BogotaMetroApp.mapper.UsuarioMapper;
import com.sena.BogotaMetroApp.persistence.models.DatosPersonales;
import com.sena.BogotaMetroApp.persistence.models.Role;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.soporte.Soporte;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SoporteMapper {

    private final UsuarioMapper usuarioMapper;

    public Soporte toEntity(SoporteRequestDTO  dto, String claveEncriptada, Role rol){
        Usuario usuario = new Usuario();
        usuario.setCorreo(dto.getCorreo());
        usuario.setClave(claveEncriptada);
        usuario.setRol(rol);
        DatosPersonales datosP = new DatosPersonales();
        datosP.setNombreCompleto(dto.getNombreCompleto());
        datosP.setTelefono(dto.getTelefono());
        datosP.setTipoDocumento(dto.getTipoDocumento());
        datosP.setNumDocumento(dto.getNumDocumento());
        datosP.setFechaNacimiento(dto.getFechaNacimiento());
        datosP.setDireccion(dto.getDireccion());

        datosP.setUsuario(usuario);
        usuario.setDatosPersonales(datosP);

        Soporte soporte = new Soporte();
        soporte.setEstado(dto.getEstado());
        soporte.setFechaCreacion(LocalDateTime.now());
        soporte.setUltimoAcceso(LocalDateTime.now());
        soporte.setUsuario(usuario);
        usuario.setSoporte(soporte);

        return  soporte;


    }
    public SoporteResponseDTO toDTO(Soporte s) {
        SoporteResponseDTO dto = new SoporteResponseDTO();
        dto.setId(s.getId());
        dto.setCorreo(s.getUsuario().getCorreo());
        dto.setEstado(s.getEstado());
        dto.setFechaCreacion(s.getFechaCreacion());
        dto.setUltimoAcceso(s.getUltimoAcceso());

        dto.setUsuario(usuarioMapper.toUsuarioInfo(s.getUsuario()));

        return dto;
    }
}
