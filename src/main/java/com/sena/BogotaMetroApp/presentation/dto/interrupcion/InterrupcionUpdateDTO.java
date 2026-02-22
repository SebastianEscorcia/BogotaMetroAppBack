package com.sena.BogotaMetroApp.presentation.dto.interrupcion;

import com.sena.BogotaMetroApp.utils.enums.EstadoInterrupcionEnum;
import com.sena.BogotaMetroApp.utils.enums.InterruptionTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InterrupcionUpdateDTO {
    private InterruptionTypeEnum tipo;
    private String descripcion;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private EstadoInterrupcionEnum estado;
    private Long idEstacion;
    private Long idLinea;
}
