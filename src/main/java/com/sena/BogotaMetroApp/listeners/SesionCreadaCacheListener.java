package com.sena.BogotaMetroApp.listeners;

import com.sena.BogotaMetroApp.events.SesionCreadaEvent;
import com.sena.BogotaMetroApp.externalservices.cache.sesionchat.ISesionChatCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SesionCreadaCacheListener {

    private final ISesionChatCacheService cacheChatService;

    @TransactionalEventListener
    public void handleSesionCreadaCacheEvent(SesionCreadaEvent event){

        cacheChatService.invalidarSesionesPendientesCache();
    }
}
