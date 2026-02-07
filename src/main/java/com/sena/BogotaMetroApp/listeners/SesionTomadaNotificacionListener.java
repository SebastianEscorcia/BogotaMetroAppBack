package com.sena.BogotaMetroApp.listeners;

import com.sena.BogotaMetroApp.events.SesionTomadaEvent;
import com.sena.BogotaMetroApp.services.notificationwebsocket.dashboard.IDashboardNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SesionTomadaNotificacionListener {
    private final IDashboardNotifier dashboardNotifier;

    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMMIT)
    public void handleSesionTomadaEvent(SesionTomadaEvent event){
        Map<String, Long> data= Map.of(
                "idSesion", event.idSesion(),
                "idSoporte", event.idSoporte()
        );
        dashboardNotifier.notificarSesionTomada(data);
        log.info(" Notificación de sesión tomada enviada para la sesión {}", event.idSesion());
    }
}
