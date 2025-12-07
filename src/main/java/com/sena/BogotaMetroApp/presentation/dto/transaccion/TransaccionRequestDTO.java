package com.sena.BogotaMetroApp.presentation.dto.transaccion;

import com.sena.BogotaMetroApp.utils.enums.MedioPagoEnum;
import com.sena.BogotaMetroApp.utils.enums.MonedaEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransaccionRequestDTO {
    private Long idUsuario;
    @NotNull(message = "El ID de la pasarela es obligatorio")
    private Long idPasarela;

    @NotNull
    @Min(value = 1000, message = "La recarga mínima es de $1.000")
    private BigDecimal valorPagado;
    private String descripcion;
    private String referenciaPasarela;
    private MonedaEnum moneda;
    private MedioPagoEnum medioDePago;
}