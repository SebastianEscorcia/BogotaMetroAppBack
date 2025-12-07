    package com.sena.BogotaMetroApp.services.interrupcion;

    import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionResponseDTO;
    import com.sena.BogotaMetroApp.presentation.dto.interrupcion.NotificacionInterrupcionDTO;
    import com.sena.BogotaMetroApp.utils.enums.AccionNotificationEnum;
    import lombok.RequiredArgsConstructor;
    import org.springframework.messaging.simp.SimpMessagingTemplate;
    import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class InterrupcionNotificationService {
        private final SimpMessagingTemplate simpMessagingTemplate;
        private static  final String TOPIC_INTERRUPCIONES = "/topic/interrupciones";

        public void notificarNuevaInterrupcion(InterrupcionResponseDTO dto) {
            NotificacionInterrupcionDTO notificacion = new NotificacionInterrupcionDTO(AccionNotificationEnum.CREAR, dto);
            simpMessagingTemplate.convertAndSend(TOPIC_INTERRUPCIONES, notificacion);
        }

        public void notificarEliminacion(Long idInterrupcion) {
            NotificacionInterrupcionDTO notificacion = new NotificacionInterrupcionDTO(AccionNotificationEnum.ELIMINAR, idInterrupcion);
            simpMessagingTemplate.convertAndSend(TOPIC_INTERRUPCIONES, notificacion);
        }

        public void notificarActualizacion(InterrupcionResponseDTO dto) {
            NotificacionInterrupcionDTO notificacion = new NotificacionInterrupcionDTO(AccionNotificationEnum.ACTUALIZAR, dto);
            simpMessagingTemplate.convertAndSend(TOPIC_INTERRUPCIONES, notificacion);
        }

    }
