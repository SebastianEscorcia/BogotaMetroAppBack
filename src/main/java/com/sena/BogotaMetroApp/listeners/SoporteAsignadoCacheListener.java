package com.sena.BogotaMetroApp.listeners;

import com.sena.BogotaMetroApp.events.SoporteAsignadoEvent;
import com.sena.BogotaMetroApp.externalservices.cache.sesionchat.ISesionChatCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class SoporteAsignadoCacheListener {
    private final ISesionChatCacheService cacheChatService;

    @TransactionalEventListener
    public void handleSoporteAsignadoCacheEvent(SoporteAsignadoEvent event){
        Long idSesion= event.idSesion();
        Long idSoporte = event.idSoporte();
        cacheChatService.invalidarSesionesActivasCache(idSoporte);
        log.info(" Soporte {} tomó sesión {}, cachés invalidados", idSoporte, idSesion);
        cacheChatService.invalidarSesionesPendientesCache();
        log.info(" Caché de sesiones pendientes invalidado tras asignación de soporte {}", idSoporte);
        cacheChatService.invalidarTodasLasSesionesActivasCache();
    }
}
