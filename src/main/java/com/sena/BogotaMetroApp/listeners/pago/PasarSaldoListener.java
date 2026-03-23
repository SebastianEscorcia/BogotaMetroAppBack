package com.sena.BogotaMetroApp.listeners.pago;

import com.sena.BogotaMetroApp.events.pago.PasarSaldoEvent;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.RecargaNotificacionDTO;
import com.sena.BogotaMetroApp.services.notificationwebsocket.transaccion.ITransaccionNotifier;
import com.sena.BogotaMetroApp.utils.enums.MedioPagoEnum;
import com.sena.BogotaMetroApp.utils.enums.TipoTransaccion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PasarSaldoListener {
    private final ITransaccionNotifier transaccionNotifier;

    @TransactionalEventListener
    public void handlePasarSaldoEvent(PasarSaldoEvent event) {
        log.info("PasarSaldoEvent recibido: {}", event);

        try {
            RecargaNotificacionDTO recargaNotificacionDTO = RecargaNotificacionDTO.builder(

            ).tipo(TipoTransaccion.SALDO_RECIBIDO).idRecarga(event.idRecarga()).monto(event.monto()).nuevoSaldo(event.nuevoSaldoPasajeroDestino()).medioPago(event.medioPago()).fechaTransaccion(LocalDateTime.now()).mensaje("Te han realizado una recarga de " + "$" + event.monto() + "COP").build();
            transaccionNotifier.notificarRecargaExitosa(event.correPasajeroDestino(), recargaNotificacionDTO);
        } catch (Exception e) {
            log.error("Error al enviar notificación. Usuario: {}, Error: {}",
                    event.correPasajeroDestino(), e.getMessage(), e);
        }
    }
}
