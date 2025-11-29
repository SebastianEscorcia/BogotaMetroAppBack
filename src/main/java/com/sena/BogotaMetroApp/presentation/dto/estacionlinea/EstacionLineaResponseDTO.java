package com.sena.BogotaMetroApp.presentation.dto.estacionlinea;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstacionLineaResponseDTO {

    private Long idLinea;
    private Long idEstacion;
    private String nombreLinea;
    private String nombreEstacion;
    private Integer orden;
}