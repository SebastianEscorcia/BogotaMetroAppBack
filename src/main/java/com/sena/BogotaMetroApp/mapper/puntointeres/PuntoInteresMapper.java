package com.sena.BogotaMetroApp.mapper.puntointeres;

import com.sena.BogotaMetroApp.presentation.dto.puntointeres.PuntoInteresRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.puntointeres.PuntoInteresResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.puntointeres.PuntoInteres;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PuntoInteresMapper {
    private final EstacionRepository estacionRepository;

    public PuntoInteres toEntity(PuntoInteresRequestDTO dto) {
        PuntoInteres p = new PuntoInteres();
        p.setEstacion(estacionRepository.findById(dto.getIdEstacion()).orElseThrow());
        p.setNombre(dto.getNombre());
        p.setCategoria(dto.getCategoria());
        p.setLatitud(dto.getLatitud());
        p.setLongitud(dto.getLongitud());
        return p;
    }

    public PuntoInteresResponseDTO toDTO(PuntoInteres p) {
        PuntoInteresResponseDTO dto = new PuntoInteresResponseDTO();
        dto.setId(p.getId());
        dto.setIdEstacion(p.getEstacion().getId());
        dto.setNombre(p.getNombre());
        dto.setCategoria(p.getCategoria());
        dto.setLatitud(p.getLatitud());
        dto.setLongitud(p.getLongitud());
        return dto;
    }
}
