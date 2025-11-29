package com.sena.BogotaMetroApp.presentation.dto.qr;

import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ValidarQrResponse {
    private boolean permitido;
    private String mensaje;
    private String codigoEstado;
    private Long idQr;
    private TipoQr tipo;
    private Long idViaje;
    private Long idPasajero;
    private LocalDateTime fechaOperacion;
}
