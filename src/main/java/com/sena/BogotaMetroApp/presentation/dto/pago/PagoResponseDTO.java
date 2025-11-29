package com.sena.BogotaMetroApp.presentation.dto.pago;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PagoResponseDTO {
    private Long id;
    private BigDecimal valorPagado;
    private LocalDateTime fechaPago;
    private String descripcion;
    private String referenciaPasarela;
    private String moneda;
    private String medioDePago;
    private Long idUsuario;
    private String nombreUsuario;
    private Long idPasarela;
    private String nombrePasarela;
}
