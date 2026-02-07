package com.sena.BogotaMetroApp.listeners;

import com.sena.BogotaMetroApp.events.SesionCreadaEvent;
import com.sena.BogotaMetroApp.externalservices.ChatRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SesionCreadaRedisListener {

    private final ChatRedisService chatRedisService;
    @TransactionalEventListener
    public void handleSesionCreadaEvent(SesionCreadaEvent event) {
        Long idSesion = event.dto().getId();
        chatRedisService.actualizarActividad(idSesion);
    }
}
