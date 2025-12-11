package com.sena.BogotaMetroApp.presentation.dto.torniquete;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidarIngresoDTO {

    @NotNull(message = "El contenido del QR es obligatorio")
    private String contenidoQr;

    @NotNull(message = "El ID de la estación es obligatorio")
    private Long idEstacion;
}
