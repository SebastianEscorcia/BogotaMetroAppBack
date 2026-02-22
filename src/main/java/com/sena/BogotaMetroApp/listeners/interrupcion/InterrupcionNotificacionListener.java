package com.sena.BogotaMetroApp.listeners.interrupcion;

import com.sena.BogotaMetroApp.events.InterrupcionEvent;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.NotificacionInterrupcionDTO;
import com.sena.BogotaMetroApp.services.notificationwebsocket.interrupcion.IInterrupcionWebSocketNotifier;
import com.sena.BogotaMetroApp.utils.enums.AccionNotificationEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listener que escucha los eventos de interrupción y notifica al frontend vía WebSocket.
 * La validación del bloqueo de ingreso se hace dinámicamente en el servicio de torniquete
 * consultando el repositorio de interrupciones.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InterrupcionNotificacionListener {
    private final IInterrupcionWebSocketNotifier interrupcionWebSocketNotifier;

    @TransactionalEventListener
    public void handleInterrupcionEvent(InterrupcionEvent event) {
        log.info("Evento de interrupción recibido - Acción: {}", event.accion());

        try {
            switch (event.accion()) {
                case CREAR -> handleInterrupcionCreada(event);
                case ACTUALIZAR -> handleInterrupcionActualizada(event);
                case SOLUCIONAR -> handleInterrupcionActualizada(event); // Solucionar es una actualización de estado
                case ELIMINAR -> handleInterrupcionEliminada(event);
            }
        } catch (Exception e) {
            log.error("Error al procesar evento de interrupción. Acción: {}, Error: {}",
                    event.accion(), e.getMessage(), e);
        }
    }

    private void handleInterrupcionCreada(InterrupcionEvent event) {
        InterrupcionResponseDTO interrupcion = (InterrupcionResponseDTO) event.payload();
        log.info("Interrupción creada - ID: {}, Estación: {}",
                interrupcion.getId(), interrupcion.getNombreEstacion());

        NotificacionInterrupcionDTO notificacion = new NotificacionInterrupcionDTO(
                AccionNotificationEnum.CREAR,
                interrupcion
        );
        interrupcionWebSocketNotifier.notificarNuevaInterrupcion(notificacion);
    }

    private void handleInterrupcionActualizada(InterrupcionEvent event) {
        InterrupcionResponseDTO interrupcion = (InterrupcionResponseDTO) event.payload();
        log.info("Interrupción actualizada - ID: {}, Estado: {}",
                interrupcion.getId(), interrupcion.getEstado());

        NotificacionInterrupcionDTO notificacion = new NotificacionInterrupcionDTO(
                AccionNotificationEnum.ACTUALIZAR,
                interrupcion
        );
        interrupcionWebSocketNotifier.notificarActualizacion(notificacion);
    }

    private void handleInterrupcionEliminada(InterrupcionEvent event) {
        Long idInterrupcion = (Long) event.payload();
        log.info("Interrupción eliminada - ID: {}", idInterrupcion);

        NotificacionInterrupcionDTO notificacion = new NotificacionInterrupcionDTO(
                AccionNotificationEnum.ELIMINAR,
                idInterrupcion
        );
        interrupcionWebSocketNotifier.notificarEliminacion(idInterrupcion);
    }
}
