package com.sena.BogotaMetroApp.services;

import com.sena.BogotaMetroApp.presentation.dto.MensajeDTO;
import com.sena.BogotaMetroApp.services.notificationwebsocket.IGroupNotifier;
import com.sena.BogotaMetroApp.utils.enums.TipoRemitenteEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * Servicio encargado de gestionar las notificaciones en el chat.
 */
@Service
@RequiredArgsConstructor
public class ChatNotificationService {

    private final IGroupNotifier groupNotifier;

    public void sendNewNotificationMessage(Long sessionId, MensajeDTO mensajeDTO) {
        groupNotifier.enviarASalaChat(sessionId, mensajeDTO);
    }
    public void notificarError(Long idSesion, String errorMsg) {
        MensajeDTO errorDTO = construirMensajeSistema("ERROR: " + errorMsg);
        groupNotifier.enviarASalaChat(idSesion, errorDTO);
    }

    public void notificarCierre(Long idSesion, String motivo) {
        MensajeDTO avisoDTO = construirMensajeSistema(motivo);
        groupNotifier.enviarASalaChat(idSesion, avisoDTO);
    }

    private MensajeDTO construirMensajeSistema(String contenido) {
        MensajeDTO dto = new MensajeDTO();
        dto.setContenido(contenido);
        dto.setTipoRemitente(TipoRemitenteEnum.SISTEMA);
        dto.setFechaEnvio(LocalDateTime.now());
        dto.setRemitenteId(0L);
        return dto;
    }
}
