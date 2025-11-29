package com.sena.BogotaMetroApp.presentation.dto.viajeruta;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViajeRutaRequestDTO {
    @NotNull
    private Long idViaje;

    @NotNull
    private Long idRuta;
}
