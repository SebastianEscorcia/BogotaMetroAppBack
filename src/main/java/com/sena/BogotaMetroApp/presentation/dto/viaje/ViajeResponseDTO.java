package com.sena.BogotaMetroApp.presentation.dto.viaje;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ViajeResponseDTO {

    private Long id;
    private String nombreViaje;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String descripcion;
    private Double presupuesto;
    private Integer estadoViaje;

    // nombres de las rutas asociadas
    private List<String> rutas;
}
