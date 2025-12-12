package com.sena.BogotaMetroApp.services.puntointeres;

import com.sena.BogotaMetroApp.presentation.dto.puntointeres.PuntoInteresRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.puntointeres.PuntoInteresResponseDTO;

import java.util.List;

public interface IPuntoInteresService {
    PuntoInteresResponseDTO crear(PuntoInteresRequestDTO dto);
    List<PuntoInteresResponseDTO> listar();
    PuntoInteresResponseDTO actualizar(Long id, PuntoInteresRequestDTO dto);
    public void eliminar(Long id);
}
