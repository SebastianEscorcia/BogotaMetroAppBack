package com.sena.BogotaMetroApp.presentation.dto.estacionlinea;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstacionLineaRequestDTO {

    private Long idLinea;
    private Long idEstacion;
    private Integer orden;
}