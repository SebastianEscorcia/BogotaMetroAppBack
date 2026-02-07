package com.sena.BogotaMetroApp.listeners;

import com.sena.BogotaMetroApp.events.SesionCerradaEvent;
import com.sena.BogotaMetroApp.externalservices.ChatRedisService;
import com.sena.BogotaMetroApp.externalservices.cache.sesionchat.ISesionChatCacheService;
import com.sena.BogotaMetroApp.services.notificationwebsocket.chat.IChatNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class SesionCerradaListener {
    private final IChatNotifier iChatNotifier;
    private final ChatRedisService redisService;
    private final ISesionChatCacheService sesionChatCacheService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSesionCerradaEvent(SesionCerradaEvent event){
        iChatNotifier.notificarEvento(
                event.idSesion(),
                "La sesión de chat ha sido cerrada."
        );
        redisService.eliminarTemporizador(event.idSesion());

        sesionChatCacheService.invalidarSesionesActivasCache(event.idUsuario());
        log.info(" Sesión {} cerrada, caché del soporte {} invalidado", event.idSesion(), event.idUsuario());
    }
}
