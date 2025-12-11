package com.sena.BogotaMetroApp.presentation.dto.tarifasistema;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TarifaSistemaRequestDTO {
    @NotNull(message = "El valor de la tarifa es obligatorio")
    @DecimalMin(value = "0.01", message = "La tarifa debe ser mayor a cero")
    private BigDecimal valorTarifa;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;

    private Boolean activa;
}
