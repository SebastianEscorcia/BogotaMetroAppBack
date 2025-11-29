package com.sena.BogotaMetroApp.services.estacion;

import com.sena.BogotaMetroApp.presentation.dto.estacion.EstacionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.estacion.EstacionResponseDTO;

import java.util.List;

public interface IEstacionServices {
    EstacionResponseDTO crear(EstacionRequestDTO dto);

    EstacionResponseDTO obtener(Long id);

    List<EstacionResponseDTO> listar();

    EstacionResponseDTO actualizar(Long id, EstacionRequestDTO dto);

    void eliminar(Long id);
}
