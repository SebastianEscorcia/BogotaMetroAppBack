package com.sena.BogotaMetroApp.presentation.dto.horariosistema;

import com.sena.BogotaMetroApp.utils.enums.DiaSemana;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class HorarioSistemaRequestDTO {
    @NotNull(message = "El día es obligatorio")
    private DiaSemana dia;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    private Boolean activo = true;
}