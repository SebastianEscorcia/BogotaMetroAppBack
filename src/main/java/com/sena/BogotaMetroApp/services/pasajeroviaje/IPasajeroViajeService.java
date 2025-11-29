package com.sena.BogotaMetroApp.services.pasajeroviaje;

import com.sena.BogotaMetroApp.presentation.dto.pasajeroviaje.PasajeroViajeRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajeroviaje.PasajeroViajeResponseDTO;
import java.util.List;
public interface IPasajeroViajeService {
    PasajeroViajeResponseDTO registrarViaje(PasajeroViajeRequestDTO dto);

    PasajeroViajeResponseDTO obtenerTicket(Long idPasajero, Long idViaje);

    List<PasajeroViajeResponseDTO> obtenerViajesPorPasajero(Long idPasajero);

    List<PasajeroViajeResponseDTO> obtenerPasajerosDeViaje(Long idViaje);
}
