package com.sena.BogotaMetroApp.mapper;

import com.sena.BogotaMetroApp.presentation.dto.estacionlinea.EstacionLineaResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.estacion.EstacionLinea;
import org.springframework.stereotype.Component;

@Component
public class EstacionLineaMapper {
    public EstacionLineaResponseDTO toDTO(EstacionLinea el) {
        EstacionLineaResponseDTO dto = new EstacionLineaResponseDTO();

        dto.setIdLinea(el.getLinea().getId());
        dto.setIdEstacion(el.getEstacion().getId());
        dto.setNombreLinea(el.getLinea().getNombre());
        dto.setNombreEstacion(el.getEstacion().getNombre());
        dto.setOrden(el.getOrden());

        return dto;
    }
}
