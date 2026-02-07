package com.sena.BogotaMetroApp.listeners;

import com.sena.BogotaMetroApp.persistence.repository.SesionChatRepository;
import com.sena.BogotaMetroApp.services.notificationwebsocket.chat.ChatWebSocketNotifier;
import com.sena.BogotaMetroApp.utils.enums.EstadoSesionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Listener para eventos de expiración de claves en Redis relacionados con sesiones de chat.
 * Cuando una clave de sesión de chat expira, este listener cierra la sesión en la base de datos
 * y notifica a los usuarios involucrados.
 */
@Component
@Slf4j
public class ChatExpirationInactivityRedisListener extends KeyExpirationEventMessageListener {

    private final SesionChatRepository sesionChatRepository;
    private final ChatWebSocketNotifier chatWebSocketNotifier;

    public ChatExpirationInactivityRedisListener(RedisMessageListenerContainer listenerContainer, SesionChatRepository sesionChatRepository,
                                                 ChatWebSocketNotifier chatWebSocketNotifier) {
        super(listenerContainer);
        this.chatWebSocketNotifier = chatWebSocketNotifier;
        this.sesionChatRepository = sesionChatRepository;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        if (expiredKey.startsWith("chat:timeout:")) {
            String idString = expiredKey.replace("chat:timeout:", "");
            Long idSesion = Long.parseLong(idString);

            log.info("Redis Event: La sesión de chat con ID {} ha expirado.", idSesion);
            cerrarSesionChat(idSesion);

        }
    }

    private void cerrarSesionChat(Long idSesion) {
        sesionChatRepository.findById(idSesion).ifPresent(sesionChat -> {
            if (sesionChat.getEstado() == EstadoSesionEnum.ACTIVO) {
                sesionChat.setEstado(EstadoSesionEnum.CERRADO);
                sesionChat.setFechaCierre(LocalDateTime.now());
                sesionChatRepository.save(sesionChat);

                chatWebSocketNotifier.notificarEvento(idSesion, "Sesión cerrada por inactividad (Redis).");
                log.info("✅ Sesión {} cerrada correctamente en BD y notificada.", idSesion);
            }
        });
    }
}
