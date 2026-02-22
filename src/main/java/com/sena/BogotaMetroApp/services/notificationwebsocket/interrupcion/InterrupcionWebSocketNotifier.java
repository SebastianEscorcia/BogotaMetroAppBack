package com.sena.BogotaMetroApp.services.notificationwebsocket.interrupcion;

import com.sena.BogotaMetroApp.services.notificationwebsocket.IBroadcastNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterrupcionWebSocketNotifier implements  IInterrupcionWebSocketNotifier {

    private final IBroadcastNotifier broadcastNotifier;

    @Override
    public void notificarNuevaInterrupcion(Object payload) {
        broadcastNotifier.enviarAInterrupciones(payload);
    }
    @Override
    public void notificarEliminacion(Long idInterrupcion) {
        broadcastNotifier.enviarAInterrupciones(idInterrupcion);
    }

    @Override
    public void notificarActualizacion(Object payload) {
        broadcastNotifier.enviarAInterrupciones(payload);
    }

}
