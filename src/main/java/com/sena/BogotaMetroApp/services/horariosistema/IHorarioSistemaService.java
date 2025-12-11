package com.sena.BogotaMetroApp.services.horariosistema;

import com.sena.BogotaMetroApp.presentation.dto.horariosistema.HorarioSistemaRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.horariosistema.HorarioSistemaResponseDTO;
import com.sena.BogotaMetroApp.utils.enums.DiaSemana;

import java.util.List;

public interface IHorarioSistemaService {
    HorarioSistemaResponseDTO crearHorario(HorarioSistemaRequestDTO request);
    HorarioSistemaResponseDTO obtenerHorarioPorDia(DiaSemana dia);
    List<HorarioSistemaResponseDTO> obtenerTodosHorarios();
    HorarioSistemaResponseDTO actualizarHorario(Long id, HorarioSistemaRequestDTO request);
    void eliminarHorario(Long id);
    boolean validarHorarioActual();  // Método para validar si ahora está dentro del horario
}