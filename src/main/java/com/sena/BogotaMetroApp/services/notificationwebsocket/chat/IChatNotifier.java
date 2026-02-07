package com.sena.BogotaMetroApp.services.notificationwebsocket.chat;

import com.sena.BogotaMetroApp.presentation.dto.MensajeDTO;

public interface IChatNotifier {
    void enviarMensaje(Long idSesion, MensajeDTO mensaje);
    void notificarError(Long idSesion, String error);
    void notificarEvento(Long idSesion, String mensaje);
}
