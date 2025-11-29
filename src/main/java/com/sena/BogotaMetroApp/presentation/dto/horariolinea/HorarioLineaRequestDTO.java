package com.sena.BogotaMetroApp.presentation.dto.horariolinea;

import com.sena.BogotaMetroApp.utils.enums.DiaSemana;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class HorarioLineaRequestDTO {
    private Long idLinea;
    private DiaSemana dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
