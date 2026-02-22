package com.sena.BogotaMetroApp.services.estacion;

import com.sena.BogotaMetroApp.presentation.dto.estacionlinea.EstacionLineaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.estacionlinea.EstacionLineaResponseDTO;

import java.util.List;

public interface IEstacionLineaServices {
    EstacionLineaResponseDTO crear(EstacionLineaRequestDTO dto);

    List<EstacionLineaResponseDTO> listar();

    void eliminar(Long idLinea, Long idEstacion);

}
