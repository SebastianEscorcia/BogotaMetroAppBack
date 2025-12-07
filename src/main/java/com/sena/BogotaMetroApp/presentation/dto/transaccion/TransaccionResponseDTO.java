package com.sena.BogotaMetroApp.presentation.dto.transaccion;

import com.sena.BogotaMetroApp.utils.enums.MedioPagoEnum;
import com.sena.BogotaMetroApp.utils.enums.MonedaEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransaccionResponseDTO {
    private Long id;
    private BigDecimal valorPagado;
    private LocalDateTime fechaPago;
    private String descripcion;
    private String referenciaPasarela;
    private MonedaEnum moneda;
    private MedioPagoEnum medioDePago;
    private Long idUsuario;
    private String nombreUsuario;
    private Long idPasarela;
    private String nombrePasarela;
}
