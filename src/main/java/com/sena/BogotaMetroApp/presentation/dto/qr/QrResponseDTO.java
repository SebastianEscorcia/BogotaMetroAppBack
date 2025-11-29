package com.sena.BogotaMetroApp.presentation.dto.qr;

import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QrResponseDTO {
    private Long id;
    private TipoQr tipo;
    private String contenidoQr;
    private LocalDateTime fechaGeneracion;
}
