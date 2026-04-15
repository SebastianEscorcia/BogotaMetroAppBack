package com.sena.BogotaMetroApp.presentation.dto.estacion;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EstacionResponseDTO {

    private Long id;
    private String nombre;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private Boolean esAccesible;
    private String tipo;
    
}
