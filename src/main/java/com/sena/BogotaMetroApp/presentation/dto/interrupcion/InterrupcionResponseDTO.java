package com.sena.BogotaMetroApp.presentation.dto.interrupcion;

import com.sena.BogotaMetroApp.utils.enums.EstadoInterrupcionEnum;
import com.sena.BogotaMetroApp.utils.enums.InterruptionTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InterrupcionResponseDTO {

    private Long id;
    private Long idEstacion;
    private Long idLinea;
    private String nombreEstacion;
    private String nombreLinea;
    private InterruptionTypeEnum tipo;
    private String descripcion;
    private LocalDateTime inicio;
    private LocalDateTime fin;

    private EstadoInterrupcionEnum estado;
}
