package com.sena.BogotaMetroApp.services.interrupcion;

import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.NotificacionInterrupcionDTO;
import com.sena.BogotaMetroApp.services.notificationwebsocket.IBroadcastNotifier;
import com.sena.BogotaMetroApp.utils.enums.AccionNotificationEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterrupcionNotificationService {

    private final IBroadcastNotifier broadcastNotifier;

    public void notificarNuevaInterrupcion(InterrupcionResponseDTO dto) {
        NotificacionInterrupcionDTO notificacion = new NotificacionInterrupcionDTO(AccionNotificationEnum.CREAR, dto);
        broadcastNotifier.enviarAInterrupciones(notificacion);
    }

    public void notificarEliminacion(Long idInterrupcion) {
        NotificacionInterrupcionDTO notificacion = new NotificacionInterrupcionDTO(AccionNotificationEnum.ELIMINAR, idInterrupcion);
        broadcastNotifier.enviarAInterrupciones(notificacion);
    }

    public void notificarActualizacion(InterrupcionResponseDTO dto) {
        NotificacionInterrupcionDTO notificacion = new NotificacionInterrupcionDTO(AccionNotificationEnum.ACTUALIZAR, dto);
        broadcastNotifier.enviarAInterrupciones(notificacion);
    }

}
