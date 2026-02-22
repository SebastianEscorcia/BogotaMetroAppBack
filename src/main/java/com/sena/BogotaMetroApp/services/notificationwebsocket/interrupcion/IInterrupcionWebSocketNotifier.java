package com.sena.BogotaMetroApp.services.notificationwebsocket.interrupcion;


public interface IInterrupcionWebSocketNotifier {
    void notificarNuevaInterrupcion(Object payload);
    void notificarEliminacion(Long idInterrupcion);
    void notificarActualizacion(Object payload);
}
