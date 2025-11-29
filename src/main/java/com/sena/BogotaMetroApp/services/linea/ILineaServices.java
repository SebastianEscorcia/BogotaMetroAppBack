package com.sena.BogotaMetroApp.services.linea;

import com.sena.BogotaMetroApp.presentation.dto.linea.LineaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.linea.LineaResponseDTO;

import java.util.List;

public interface ILineaServices {
    LineaResponseDTO crear(LineaRequestDTO dto);

    LineaResponseDTO obtener(Long id);

    List<LineaResponseDTO> listar();

    LineaResponseDTO actualizar(Long id, LineaRequestDTO dto);

    void eliminar(Long id);
}
