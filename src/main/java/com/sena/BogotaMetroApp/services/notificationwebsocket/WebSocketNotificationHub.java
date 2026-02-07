package com.sena.BogotaMetroApp.services.notificationwebsocket;

import com.sena.BogotaMetroApp.presentation.dto.sesionchat.SesionChatResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Servicio que implementa los notifiers para enviar notificaciones a través de WebSocket.
 */
@Service
@RequiredArgsConstructor
public class WebSocketNotificationHub implements IBroadcastNotifier, IGroupNotifier, IUserNotifier {

    private final SimpMessagingTemplate template;

    @Override
    public void enviarAInterrupciones(Object payload) {
        template.convertAndSend("/topic/interrupciones", payload);
    }

    @Override
    public void enviarSesionTomada(Object payload) {
        template.convertAndSend("/topic/sesion-tomada", payload);
    }

    @Override
    public void enviarNuevaSesionPendiente(SesionChatResponseDTO dto) {
        template.convertAndSend("/topic/sesiones-pendientes", dto);
    }

    @Override
    public void enviarASalaChat(Long idSala, Object payload) {
        template.convertAndSend("/topic/sala/" + idSala, payload);
    }

    @Override
    public void enviarAUsuario(String username, Object payload) {
        template.convertAndSendToUser(username, "/queue/notificaciones", payload);
    }
}
