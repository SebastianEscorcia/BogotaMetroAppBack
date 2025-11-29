package com.sena.BogotaMetroApp.presentation.dto.pasarela;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasarelaResponseDTO {
    private Long id;
    private String nombre;
    private Integer codigo;
    private String pais;
}
