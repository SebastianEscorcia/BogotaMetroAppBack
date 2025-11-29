package com.sena.BogotaMetroApp.mapper;

import com.sena.BogotaMetroApp.presentation.dto.linea.LineaResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.Linea;
import org.springframework.stereotype.Component;

@Component
public class LineaMapper {
    public LineaResponseDTO toDTO(Linea linea) {
        LineaResponseDTO dto = new LineaResponseDTO();

        dto.setId(linea.getId());
        dto.setNombre(linea.getNombre());
        dto.setColor(linea.getColor());
        dto.setFrecuenciaMinutos(linea.getFrecuenciaMinutos());

        return dto;
    }
}
