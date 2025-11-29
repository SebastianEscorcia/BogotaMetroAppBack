package com.sena.BogotaMetroApp.mapper;

import com.sena.BogotaMetroApp.presentation.dto.horariolinea.HorarioLineaResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.horariolinea.HorarioLinea;
import org.springframework.stereotype.Component;

@Component
public class HorarioLineaMapper {
    public HorarioLineaResponseDTO toDTO(HorarioLinea h) {
        HorarioLineaResponseDTO dto = new HorarioLineaResponseDTO();
        dto.setId(h.getId());
        dto.setIdLinea(h.getLinea().getId());
        dto.setNombreLinea(h.getLinea().getNombre());
        dto.setDia(h.getDia());
        dto.setHoraInicio(h.getHoraInicio());
        dto.setHoraFin(h.getHoraFin());

        return dto;
    }
}
