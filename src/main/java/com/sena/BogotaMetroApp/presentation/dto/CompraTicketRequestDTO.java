package com.sena.BogotaMetroApp.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompraTicketRequestDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El ID del viaje es obligatorio")
    private Long idViaje;
}
