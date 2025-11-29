package com.sena.BogotaMetroApp.presentation.dto.puntointeres;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PuntoInteresRequestDTO {
    @NotNull
    private Long idEstacion;
    private String nombre;
    private String categoria;
    private BigDecimal latitud;
    private BigDecimal longitud;
}
