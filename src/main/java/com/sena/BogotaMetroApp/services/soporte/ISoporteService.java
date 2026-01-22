package com.sena.BogotaMetroApp.services.soporte;

import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteUpdateDTO;

import java.util.List;

public interface ISoporteService {
    SoporteResponseDTO registrar (SoporteRequestDTO dto);
    List<SoporteResponseDTO> listarSoportes(String busqueda);
    SoporteResponseDTO obtenerPorId(Long id);
    SoporteResponseDTO actualizar(Long id, SoporteUpdateDTO dto);
    SoporteResponseDTO obtenerPorCorreo(String correo);
    void eliminar(Long id);
}
