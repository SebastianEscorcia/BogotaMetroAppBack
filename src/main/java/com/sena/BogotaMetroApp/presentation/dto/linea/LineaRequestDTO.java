package com.sena.BogotaMetroApp.presentation.dto.linea;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LineaRequestDTO {
    private String nombre;
    private String color;
    private Integer frecuenciaMinutos;
}
