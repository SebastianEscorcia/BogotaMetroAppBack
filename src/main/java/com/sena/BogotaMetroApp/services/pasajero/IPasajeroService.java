package com.sena.BogotaMetroApp.services.pasajero;

import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroUpdateDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.RegistroPasajeroUnificadoDTO;

import java.util.List;

public interface IPasajeroService {
    PasajeroResponseDTO obtener(Long id);
    List<PasajeroResponseDTO> listarTodos();
    void eliminar(Long id);
    PasajeroResponseDTO actualizar(Long id, PasajeroUpdateDTO dto);

    PasajeroResponseDTO registrarConUsuario(RegistroPasajeroUnificadoDTO dto);
}
