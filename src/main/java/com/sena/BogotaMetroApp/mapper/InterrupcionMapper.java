package com.sena.BogotaMetroApp.mapper;

import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.interrupcion.Interrupcion;
import org.springframework.stereotype.Component;

@Component
public class InterrupcionMapper {
    public InterrupcionResponseDTO toDTO(Interrupcion inter) {

        InterrupcionResponseDTO dto = new InterrupcionResponseDTO();
        dto.setId(inter.getId());
        dto.setIdEstacion(inter.getEstacion().getId());
        dto.setNombreEstacion(inter.getEstacion().getNombre());
        dto.setIdLinea(inter.getLinea().getId());
        dto.setNombreLinea(inter.getLinea().getNombre());
        dto.setTipo(inter.getTipo());
        dto.setDescripcion(inter.getDescripcion());
        dto.setInicio(inter.getInicio());
        dto.setFin(inter.getFin());
        dto.setEstado(inter.getEstado());
        return dto;
    }
}
