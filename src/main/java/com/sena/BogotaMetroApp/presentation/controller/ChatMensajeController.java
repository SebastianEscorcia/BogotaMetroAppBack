package com.sena.BogotaMetroApp.presentation.controller;

import com.sena.BogotaMetroApp.presentation.dto.MensajeDTO;
import com.sena.BogotaMetroApp.services.ChatNotificationService;
import com.sena.BogotaMetroApp.services.chatmensaje.IChatMensajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller("/ws-metro")
@RequiredArgsConstructor
public class ChatMensajeController {

    private final IChatMensajeService chatMensajeService;
    private final ChatNotificationService chatNotificationService;

    /**
     * Recibe un mensaje desde el cliente.
     * Ruta de envío desde el front: /app/chat/{idSesion}
     */
    @MessageMapping("/chat/{idSesion}")
    public void enviarMensaje(@DestinationVariable Long idSesion, @Payload MensajeDTO mensajeDTO) {
        try {
            MensajeDTO mensajeGuardado = chatMensajeService.procesarYGuardarMensaje(idSesion, mensajeDTO);

           chatNotificationService.sendNewNotificationMessage(idSesion, mensajeGuardado);

        } catch (RuntimeException e) {
            chatNotificationService.notificarError(idSesion, e.getMessage());
        }

    }


}
