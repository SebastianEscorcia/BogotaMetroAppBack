package com.sena.BogotaMetroApp.mapper.soporte;

import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.soporte.Soporte;
import org.springframework.stereotype.Component;

@Component
public class SoporteMapper {
    public SoporteResponseDTO toDTO(Soporte s) {
        SoporteResponseDTO dto = new SoporteResponseDTO();
        dto.setId(s.getId());
        dto.setCorreo(s.getUsuario().getCorreo());
        dto.setEstado(s.getEstado());
        dto.setFechaCreacion(s.getFechaCreacion());
        dto.setUltimoAcceso(s.getUltimoAcceso());
        return dto;
    }
}
