package com.sena.BogotaMetroApp.services.notificationwebsocket;

import com.sena.BogotaMetroApp.presentation.dto.sesionchat.SesionChatResponseDTO;

public interface IBroadcastNotifier {
    void enviarAInterrupciones(Object payload);

    void enviarSesionTomada(Object payload);

    void enviarNuevaSesionPendiente(SesionChatResponseDTO dto);
}
