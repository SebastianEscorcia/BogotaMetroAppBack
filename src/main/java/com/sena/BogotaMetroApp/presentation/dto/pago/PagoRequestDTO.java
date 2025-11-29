package com.sena.BogotaMetroApp.presentation.dto.pago;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PagoRequestDTO {
    private Long idUsuario;
    private Long idPasarela;
    private BigDecimal valorPagado;
    private String descripcion;
    private String referenciaPasarela;
    private String moneda;
    private String medioDePago;
}