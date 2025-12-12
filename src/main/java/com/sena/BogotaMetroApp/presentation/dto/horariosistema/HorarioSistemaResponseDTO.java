package com.sena.BogotaMetroApp.presentation.dto.horariosistema;

import com.sena.BogotaMetroApp.utils.enums.DiaSemana;
import lombok.Data;

import java.time.LocalTime;

@Data
public class HorarioSistemaResponseDTO {
    private Long id;
    private DiaSemana dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Boolean activo;
}