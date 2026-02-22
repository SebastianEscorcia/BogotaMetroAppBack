package com.sena.BogotaMetroApp.listeners.pago;

import com.sena.BogotaMetroApp.events.pago.RecargaRegistradaEvent;
import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.RecargaNotificacionDTO;
import com.sena.BogotaMetroApp.services.notificationwebsocket.transaccion.ITransaccionNotifier;
import com.sena.BogotaMetroApp.services.tarjetavirtual.ItarjetaVirtualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Listener unificado que procesa las recargas registradas.
 * Responsabilidades (en orden):
 * 1. Actualizar el saldo de la tarjeta
 * 2. Notificar al usuario vía WebSocket
 * Se unifican en un solo listener para GARANTIZAR el orden de ejecución.
 * Con múltiples @TransactionalEventListener, Spring NO garantiza el orden.
 *
 * @author Sebastian Escorcia
 * @version 3.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RecargaRegistradaListener {

    private final ItarjetaVirtualService tarjetaVirtualService;
    private final ITransaccionNotifier transaccionNotifier;

    /**
     * Procesa el evento de recarga: actualiza saldo y notifica al usuario.
     * El servicio recargarSaldo() tiene @Transactional(REQUIRES_NEW)
     * para garantizar que la actualización se persista correctamente.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRecargaRegistrada(RecargaRegistradaEvent event) {
        BigDecimal nuevoSaldo = BigDecimal.ZERO;
        try {
            TarjetaVirtual tarjeta = tarjetaVirtualService.recargarSaldo(event.idPasajero(), event.monto());
            nuevoSaldo = tarjeta.getSaldo();

        } catch (Exception e) {
            log.error("Error al actualizarPasajero saldo. Recarga ID: {}, Error: {}", event.idRecarga(), e.getMessage(), e);
        }

        try {

            RecargaNotificacionDTO notificacion = RecargaNotificacionDTO.builder()
                    .tipo("RECARGA_EXITOSA")
                    .idRecarga(event.idRecarga())
                    .monto(event.monto())
                    .nuevoSaldo(nuevoSaldo)
                    .medioPago(event.medioPago())
                    .fechaTransaccion(LocalDateTime.now())
                    .mensaje("¡Tu recarga fue exitosa! Tu nuevo saldo es: $" + nuevoSaldo)
                    .build();

            transaccionNotifier.notificarRecargaExitosa(event.correoUsuario(), notificacion);
            log.info("Notificación enviada exitosamente");
        } catch (Exception e) {
            log.error("Error al enviar notificación. Usuario: {}, Error: {}",
                    event.correoUsuario(), e.getMessage(), e);
        }


    }
}
