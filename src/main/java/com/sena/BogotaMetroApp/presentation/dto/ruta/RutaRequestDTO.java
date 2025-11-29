package com.sena.BogotaMetroApp.presentation.dto.ruta;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RutaRequestDTO {

    private Double distancia;
    private LocalTime horaInicioRuta;
    private LocalDate fecha;

    private Long estacionInicioId;
    private Long estacionFinId;
}
