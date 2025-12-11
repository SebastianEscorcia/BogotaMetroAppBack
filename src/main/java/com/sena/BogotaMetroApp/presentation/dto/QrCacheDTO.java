package com.sena.BogotaMetroApp.presentation.dto;

import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QrCacheDTO {

    private Long id;
    private Long usuarioId;
    private String contenido;
    private LocalDateTime fechaGeneracion;
    private TipoQr tipoQr;
    private LocalDateTime fechaExpiracion;
    private boolean consumido;
    private long version;
}
