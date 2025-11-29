package com.sena.BogotaMetroApp.services.interrupcion;

import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionResponseDTO;

import java.util.List;

public interface IInterrupcionServices {
    InterrupcionResponseDTO crear(InterrupcionRequestDTO dto);

    List<InterrupcionResponseDTO> listar();

    void eliminar(Long id);
}
