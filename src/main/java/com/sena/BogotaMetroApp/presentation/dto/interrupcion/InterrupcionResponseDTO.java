package com.sena.BogotaMetroApp.presentation.dto.interrupcion;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InterrupcionResponseDTO {

    private Long id;
    private Long idEstacion;
    private Long idLinea;
    private String nombreEstacion;
    private String nombreLinea;
    private String tipo;
    private String descripcion;
    private LocalDateTime inicio;
    private LocalDateTime fin;
}
