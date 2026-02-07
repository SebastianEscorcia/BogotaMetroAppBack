package com.sena.BogotaMetroApp.services.notificationwebsocket.dashboard;

import com.sena.BogotaMetroApp.presentation.dto.sesionchat.SesionChatResponseDTO;

public interface IDashboardNotifier {
    void notificarSesionTomada(Object payload);
    void enviarNuevaSesionPendiente(SesionChatResponseDTO dto);
}
