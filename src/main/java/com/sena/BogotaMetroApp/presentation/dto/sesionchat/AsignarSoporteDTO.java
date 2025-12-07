package com.sena.BogotaMetroApp.presentation.dto.sesionchat;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AsignarSoporteDTO {
    @NotNull
    private Long idUsuarioSoporte;
}