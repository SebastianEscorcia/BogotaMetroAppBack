package com.sena.BogotaMetroApp.presentation.dto.puntointeres;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PuntoInteresResponseDTO {
    private Long id;
    private Long idEstacion;
    private String nombre;
    private String categoria;
    private BigDecimal latitud;
    private BigDecimal longitud;
}
