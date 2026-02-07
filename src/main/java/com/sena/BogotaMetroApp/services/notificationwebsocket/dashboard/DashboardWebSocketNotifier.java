package com.sena.BogotaMetroApp.services.notificationwebsocket.dashboard;

import com.sena.BogotaMetroApp.presentation.dto.sesionchat.SesionChatResponseDTO;
import com.sena.BogotaMetroApp.services.notificationwebsocket.IBroadcastNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DashboardWebSocketNotifier implements IDashboardNotifier {

    private final IBroadcastNotifier broadcastNotifier;

    @Override
    public void notificarSesionTomada(Object payload) {

        broadcastNotifier.enviarSesionTomada(payload);

    }

    @Override
    public void enviarNuevaSesionPendiente(SesionChatResponseDTO dto) {

        broadcastNotifier.enviarNuevaSesionPendiente(dto);
    }
}

