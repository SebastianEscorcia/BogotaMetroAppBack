package com.sena.BogotaMetroApp.presentation.dto.qr;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidarQrRequest {
    @NotBlank(message = "El contenido del QR no puede estar vacío")
    private String contenidoQr ;
}
