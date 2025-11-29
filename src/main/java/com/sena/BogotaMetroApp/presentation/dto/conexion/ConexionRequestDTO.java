package com.sena.BogotaMetroApp.presentation.dto.conexion;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConexionRequestDTO {
    @NotNull
    private Long idOrigen;

    @NotNull
    private Long idDestino;

    private Integer distanciaMetros;

    private Integer tiempoMinutos;
}
