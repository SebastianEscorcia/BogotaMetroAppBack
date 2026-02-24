package com.sena.BogotaMetroApp.events.pago;

import com.sena.BogotaMetroApp.utils.enums.MedioPagoEnum;

import java.math.BigDecimal;

public record PasarSaldoEvent(Long idRecarga,
                              Long idPasajeroDestino,
                              BigDecimal nuevoSaldoPasajeroDestino,
                              MedioPagoEnum medioPago,
                              BigDecimal monto,
                              String correPasajeroDestino) {
}
