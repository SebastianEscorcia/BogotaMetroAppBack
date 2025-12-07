package com.sena.BogotaMetroApp.services;

import com.sena.BogotaMetroApp.presentation.dto.MensajeDTO;
import com.sena.BogotaMetroApp.utils.enums.TipoRemitenteEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class ChatNotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendNewNotificationMessage(Long sessionId, MensajeDTO mensajeDTO) {
        simpMessagingTemplate.convertAndSend("/topic/sala/" + sessionId, mensajeDTO);
    }
    public void notificarError(Long idSesion, String errorMsg) {
        MensajeDTO errorDTO = construirMensajeSistema("ERROR: " + errorMsg);
        simpMessagingTemplate.convertAndSend("/topic/sala/" + idSesion, errorDTO);
    }

    public void notificarCierre(Long idSesion, String motivo) {
        MensajeDTO avisoDTO = construirMensajeSistema(motivo);
        simpMessagingTemplate.convertAndSend("/topic/sala/" + idSesion, avisoDTO);
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
