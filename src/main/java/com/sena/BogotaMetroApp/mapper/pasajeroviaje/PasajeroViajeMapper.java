package com.sena.BogotaMetroApp.mapper.pasajeroviaje;


import com.sena.BogotaMetroApp.presentation.dto.pasajeroviaje.PasajeroViajeResponseDTO;

import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViaje;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PasajeroViajeMapper {

    public PasajeroViajeResponseDTO toDTO(PasajeroViaje pasajeroViaje) {
        if (pasajeroViaje == null) return null;

        PasajeroViajeResponseDTO dto = new PasajeroViajeResponseDTO();

        dto.setIdTicket(pasajeroViaje.getId());

        dto.setIdPasajero(pasajeroViaje.getPasajero().getId());
        dto.setIdViaje(pasajeroViaje.getViaje().getId());
        dto.setFechaRegistro(pasajeroViaje.getFechaRegistro().toString());
        dto.setIdQr(pasajeroViaje.getQr() != null ? pasajeroViaje.getQr().getId() : null);

        return dto;
    }
}
