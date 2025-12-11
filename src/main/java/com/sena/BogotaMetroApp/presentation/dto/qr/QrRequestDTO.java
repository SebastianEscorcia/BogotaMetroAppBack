package com.sena.BogotaMetroApp.presentation.dto.qr;

import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QrRequestDTO {
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUsuario;
    private LocalDateTime fechaExpiracion = LocalDateTime.now().plusDays(5);
    @NotNull(message = "El tipo de QR es obligatorio")
    private TipoQr tipo = TipoQr.ACCESO;

}

