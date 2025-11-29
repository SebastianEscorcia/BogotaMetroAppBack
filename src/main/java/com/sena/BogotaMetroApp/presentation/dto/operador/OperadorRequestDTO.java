package com.sena.BogotaMetroApp.presentation.dto.operador;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperadorRequestDTO {

    @NotNull
    private Long idUsuario;
}