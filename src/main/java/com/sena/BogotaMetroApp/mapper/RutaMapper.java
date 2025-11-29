package com.sena.BogotaMetroApp.mapper;

import com.sena.BogotaMetroApp.presentation.dto.ruta.RutaResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.Ruta;
import org.springframework.stereotype.Component;

@Component
public class RutaMapper {

    public RutaResponseDTO toDTO (Ruta ruta) {
        RutaResponseDTO dto = new RutaResponseDTO();

        dto.setId(ruta.getId());
        dto.setDistancia(ruta.getDistancia());
        dto.setHoraInicioRuta(ruta.getHoraInicioRuta());
        dto.setFecha(ruta.getFecha());

        dto.setEstacionInicioNombre(ruta.getEstacionInicio().getNombre());
        dto.setEstacionFinNombre(ruta.getEstacionFin().getNombre());

        return dto;
    }
}
