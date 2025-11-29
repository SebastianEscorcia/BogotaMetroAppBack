package com.sena.BogotaMetroApp.services.horariolinea;

import com.sena.BogotaMetroApp.presentation.dto.horariolinea.HorarioLineaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.horariolinea.HorarioLineaResponseDTO;

import java.util.List;

public interface IHorarioLineaServices {

    HorarioLineaResponseDTO crear(HorarioLineaRequestDTO dto);

    List<HorarioLineaResponseDTO> listar();

    void eliminar(Long id);
}
