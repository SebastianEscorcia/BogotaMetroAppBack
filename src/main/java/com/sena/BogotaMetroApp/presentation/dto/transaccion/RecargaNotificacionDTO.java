package com.sena.BogotaMetroApp.presentation.dto.transaccion;

import com.sena.BogotaMetroApp.utils.enums.MedioPagoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para notificar al usuario sobre una recarga exitosa vía WebSocket.
 * Contiene información resumida de la transacción para mostrar en tiempo real.
 *
 * @author Sebastian Escorcia
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecargaNotificacionDTO {
    private String tipo;
    private Long idRecarga;
    private BigDecimal monto;
    private BigDecimal nuevoSaldo;
    private MedioPagoEnum medioPago;
    private LocalDateTime fechaTransaccion;
    private String mensaje;
}

