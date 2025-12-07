package com.sena.BogotaMetroApp.externalservices;

import com.sena.BogotaMetroApp.persistence.repository.SesionChatRepository;
import com.sena.BogotaMetroApp.services.ChatNotificationService;
import com.sena.BogotaMetroApp.utils.enums.EstadoSesionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class ChatExpirationListener extends KeyExpirationEventMessageListener {

    private final SesionChatRepository sesionChatRepository;
    private final ChatNotificationService chatNotificationService;

    public ChatExpirationListener(RedisMessageListenerContainer listenerContainer, SesionChatRepository sesionChatRepository,
                                  ChatNotificationService chatNotificationService) {
        super(listenerContainer);
        this.chatNotificationService = chatNotificationService;
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

                chatNotificationService.notificarCierre(idSesion, "Sesión cerrada por inactividad (Redis).");
                log.info("✅ Sesión {} cerrada correctamente en BD y notificada.", idSesion);
            }
        });
    }
}
