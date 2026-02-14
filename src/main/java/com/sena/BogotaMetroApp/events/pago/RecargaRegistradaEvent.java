package com.sena.BogotaMetroApp.events.pago;

import com.sena.BogotaMetroApp.utils.enums.MedioPagoEnum;

import java.math.BigDecimal;

/**
 * Evento que se dispara cuando se registra una recarga exitosamente.
 *
 * @param idRecarga ID de la recarga registrada
 * @param idPasajero ID del usuario/pasajero
 * @param monto Valor de la recarga
 * @param medioPago Medio de pago utilizado
 * @param correoUsuario Correo del usuario para notificaciones (evita cargar relaciones LAZY)
 */
public record RecargaRegistradaEvent(
        Long idRecarga,
        Long idPasajero,
        BigDecimal monto,
        MedioPagoEnum medioPago,
        String correoUsuario
) {
}
