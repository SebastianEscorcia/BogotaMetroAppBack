package com.sena.BogotaMetroApp.presentation.dto.ruta;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RutaResponseDTO {

    private Long id;
    private Double distancia;
    private LocalTime horaInicioRuta;
    private LocalDate fecha;

    private String estacionInicioNombre;
    private String estacionFinNombre;
}
