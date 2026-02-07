package com.sena.BogotaMetroApp.services.notificationwebsocket.chat;

import com.sena.BogotaMetroApp.presentation.dto.MensajeDTO;
import com.sena.BogotaMetroApp.services.notificationwebsocket.IGroupNotifier;
import com.sena.BogotaMetroApp.utils.enums.TipoRemitenteEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * Es servicio (ADAPTADOR) encargado de gestionar el envío de los (MENSAJES) en el chat con la interfaz
 * que IGroupNotifier dónde su implementación está en el hub.
 *
 * @author Sebastian Escorcia
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ChatWebSocketNotifier implements IChatNotifier {

    private final IGroupNotifier groupNotifier;

    @Override
    public void enviarMensaje(Long idSesion, MensajeDTO mensaje) {
        groupNotifier.enviarASalaChat(idSesion, mensaje);
    }

    @Override
    public void notificarError(Long idSesion, String errorMsg) {
        MensajeDTO errorDTO = construirMensajeSistema("ERROR: " + errorMsg);
        groupNotifier.enviarASalaChat(idSesion, errorDTO);
    }

    @Override
    public void notificarEvento(Long idSesion, String mensaje) {
        MensajeDTO avisoDTO = construirMensajeSistema(mensaje);
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
