package com.sena.BogotaMetroApp.listeners;

import com.sena.BogotaMetroApp.events.SoporteAsignadoEvent;
import com.sena.BogotaMetroApp.externalservices.ChatRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class SoporteAsignadoRedisListener {
    private final ChatRedisService chatRedisService;

    @TransactionalEventListener
    public void handleSoporteAsignadoEvent(SoporteAsignadoEvent event) {
        Long idSesion = event.idSesion();
        log.info("Actualizando Redis para la sesión de chat con ID: {}", idSesion);
        chatRedisService.actualizarActividad(idSesion);
    }
}
