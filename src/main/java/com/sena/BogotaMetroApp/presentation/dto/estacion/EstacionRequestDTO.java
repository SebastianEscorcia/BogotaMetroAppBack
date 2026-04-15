package com.sena.BogotaMetroApp.presentation.dto.estacion;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EstacionRequestDTO {

    private String nombre;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private Boolean esAccesible;
    private String tipo;
   
}