package com.sena.BogotaMetroApp.presentation.dto.linea;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LineaResponseDTO {

    private Long id;
    private String nombre;
    private String color;
    private Integer frecuenciaMinutos;
}
