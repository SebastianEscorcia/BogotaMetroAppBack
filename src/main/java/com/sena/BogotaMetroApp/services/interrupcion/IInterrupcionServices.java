package com.sena.BogotaMetroApp.services.interrupcion;

import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionUpdateDTO;

import java.util.List;

public interface IInterrupcionServices {
    InterrupcionResponseDTO crear(InterrupcionRequestDTO dto);

    List<InterrupcionResponseDTO> listar();

    void eliminar(Long id);

    void marcarComoSolucionada(Long id);

    InterrupcionResponseDTO actualizar(Long id, InterrupcionUpdateDTO dto);
}
