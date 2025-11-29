package com.sena.BogotaMetroApp.presentation.dto.viaje;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ViajeRequestDTO {

    private String nombreViaje;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String descripcion;
    private Double presupuesto;
    private Integer estadoViaje;

    // IDs de las rutas que se van a asociar al viaje
    private List<Long> rutasIds;
}
