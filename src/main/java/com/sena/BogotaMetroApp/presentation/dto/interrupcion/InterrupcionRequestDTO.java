package com.sena.BogotaMetroApp.presentation.dto.interrupcion;

import com.sena.BogotaMetroApp.utils.enums.InterruptionTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InterrupcionRequestDTO {
    private Long idEstacion;
    private Long idLinea;
    private InterruptionTypeEnum tipo;
    private String descripcion;
    private LocalDateTime inicio;
    private LocalDateTime fin;
}
