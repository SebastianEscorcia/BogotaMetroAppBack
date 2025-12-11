package com.sena.BogotaMetroApp.presentation.dto.tarifasistema;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TarifaSistemaResponseDTO {

    private Long id;
    private String descripcion;
    private BigDecimal valorTarifa;
    private boolean activa;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
