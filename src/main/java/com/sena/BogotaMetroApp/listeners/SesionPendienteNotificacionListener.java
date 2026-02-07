package com.sena.BogotaMetroApp.listeners;

import com.sena.BogotaMetroApp.events.SesionCreadaEvent;
import com.sena.BogotaMetroApp.services.notificationwebsocket.dashboard.IDashboardNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@RequiredArgsConstructor
@Slf4j
public class SesionPendienteNotificacionListener {
    private final IDashboardNotifier dashboardNotifier;
    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMMIT)
    public void handleSesionPendienteEvent(SesionCreadaEvent event){

        dashboardNotifier.enviarNuevaSesionPendiente(event.dto());
        log.info(" Notificación de nueva sesión creada estado pendiente para la sesión {} en espera de soporte", event.dto().getId() );
    }
}
