package com.sena.BogotaMetroApp.presentation.dto.conexion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConexionResponseDTO {
    private Long id;
    private Long idOrigen;
    private Long idDestino;
    private Integer distanciaMetros;
    private Integer tiempoMinutos;
}
