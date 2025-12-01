package com.sena.BogotaMetroApp.presentation.dto.mapa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class MapaEstacionDTO {
    private Long id;
    private String nombre;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private Integer orden;
}
