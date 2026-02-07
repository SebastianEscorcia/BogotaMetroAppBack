package com.sena.BogotaMetroApp.listeners;

import com.sena.BogotaMetroApp.events.SoporteAsignadoEvent;
import com.sena.BogotaMetroApp.services.notificationwebsocket.chat.IChatNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class SoporteAsignadoNotificacionListener {
    private final IChatNotifier iChatNotifier;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSoporteAsignadoEvent(SoporteAsignadoEvent event) {
        Long idSesion = event.idSesion();
        String nombreSoporte = event.nombreSoporte();
        String mensaje = "El soporte " + nombreSoporte + " ha sido asignado a tu sesión de chat.";
        iChatNotifier.notificarEvento(idSesion, mensaje);
    }
}
