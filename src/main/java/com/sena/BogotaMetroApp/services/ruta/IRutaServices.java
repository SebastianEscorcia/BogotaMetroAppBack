package com.sena.BogotaMetroApp.services.ruta;

import com.sena.BogotaMetroApp.presentation.dto.ruta.RutaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.ruta.RutaResponseDTO;

import java.util.List;

public interface IRutaServices {
    RutaResponseDTO crear(RutaRequestDTO dto);

    RutaResponseDTO obtener(Long id);

    List<RutaResponseDTO> listar();

    RutaResponseDTO actualizar(Long id, RutaRequestDTO dto);

    void eliminar(Long id);
}
