package com.sena.BogotaMetroApp.services.viaje;

import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.viaje.ViajeRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.viaje.ViajeResponseDTO;

import java.util.List;

public interface IViajeServices {
    ViajeResponseDTO crearViaje(ViajeRequestDTO dto);

    List<ViajeResponseDTO> listarViajes();

    ViajeResponseDTO obtenerViaje(Long id);

}
