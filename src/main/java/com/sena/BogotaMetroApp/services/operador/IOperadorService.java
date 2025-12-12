package com.sena.BogotaMetroApp.services.operador;

import com.sena.BogotaMetroApp.presentation.dto.operador.OperadorResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.operador.RegistroOperadorDTO;

import java.util.List;

public interface IOperadorService {
    OperadorResponseDTO registrar(RegistroOperadorDTO dto);
    List<OperadorResponseDTO> listarOperadores(String busqueda);
    OperadorResponseDTO actualizar(Long id, RegistroOperadorDTO dto); // Reusamos el DTO o crea uno UpdateDTO
    void eliminar(Long id);
    OperadorResponseDTO obtenerPorId(Long id);

    void reactivar(Long id);
}
