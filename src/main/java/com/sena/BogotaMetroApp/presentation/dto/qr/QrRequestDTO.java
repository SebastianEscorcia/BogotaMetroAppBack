package com.sena.BogotaMetroApp.presentation.dto.qr;

import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QrRequestDTO {
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUsuario;
    private Long idPago;   // opcional si tipo == PAGO
    private Long idViaje;
    @NotNull(message = "El tipo de QR es obligatorio")
    private TipoQr tipo;

}

