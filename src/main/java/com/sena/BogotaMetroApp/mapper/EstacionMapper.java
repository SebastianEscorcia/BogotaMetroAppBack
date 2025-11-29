package com.sena.BogotaMetroApp.mapper;

import com.sena.BogotaMetroApp.presentation.dto.estacion.EstacionResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import org.springframework.stereotype.Component;

@Component
public class EstacionMapper {
    public EstacionResponseDTO toDTO(Estacion estacion) {
        EstacionResponseDTO dto = new EstacionResponseDTO();
        dto.setId(estacion.getId());
        dto.setNombre(estacion.getNombre());
        dto.setLatitud(estacion.getLatitud());
        dto.setLongitud(estacion.getLongitud());
        dto.setEsAccesible(estacion.getEsAccesible());
        dto.setTipo(estacion.getTipo());

        return dto;
    }
}
