package com.sena.BogotaMetroApp.mapper;

import com.sena.BogotaMetroApp.presentation.dto.viaje.ViajeResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.Viaje;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ViajeMapper {

    public ViajeResponseDTO toDTO(Viaje viaje) {

        ViajeResponseDTO dto = new ViajeResponseDTO();
        dto.setId(viaje.getId());
        dto.setNombreViaje(viaje.getNombreViaje());
        dto.setFechaHoraInicio(viaje.getFechaHoraInicio());
        dto.setFechaHoraFin(viaje.getFechaHoraFin());
        dto.setDescripcion(viaje.getDescripcion());
        dto.setPresupuesto(viaje.getPresupuesto());
        dto.setEstadoViaje(viaje.getEstadoViaje());

        List<String> rutas = viaje.getRutas()
                .stream()
                .map(r -> "Ruta " + r.getId())
                .toList();

        dto.setRutas(rutas);

        return dto;
    }
}

