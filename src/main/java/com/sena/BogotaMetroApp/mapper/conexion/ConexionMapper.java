package com.sena.BogotaMetroApp.mapper.conexion;

import com.sena.BogotaMetroApp.presentation.dto.conexion.ConexionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.conexion.ConexionResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.conexion.Conexion;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConexionMapper {

    private final EstacionRepository estacionRepository;
    public Conexion toEntity(ConexionRequestDTO dto) {
        Conexion c = new Conexion();
        c.setOrigen(estacionRepository.findById(dto.getIdOrigen()).orElseThrow());
        c.setDestino(estacionRepository.findById(dto.getIdDestino()).orElseThrow());
        c.setDistanciaMetros(dto.getDistanciaMetros());
        c.setTiempoMinutos(dto.getTiempoMinutos());
        return c;
    }

    public ConexionResponseDTO toDTO(Conexion c) {
        ConexionResponseDTO dto = new ConexionResponseDTO();
        dto.setId(c.getId());
        dto.setIdOrigen(c.getOrigen().getId());
        dto.setIdDestino(c.getDestino().getId());
        dto.setDistanciaMetros(c.getDistanciaMetros());
        dto.setTiempoMinutos(c.getTiempoMinutos());
        return dto;
    }
}
