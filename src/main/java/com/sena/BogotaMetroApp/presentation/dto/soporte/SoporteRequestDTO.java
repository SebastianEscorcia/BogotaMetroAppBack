package com.sena.BogotaMetroApp.presentation.dto.soporte;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class SoporteRequestDTO {

    @NotNull
    private Long idUsuario;

    @NotNull
    private Integer estado;
}
