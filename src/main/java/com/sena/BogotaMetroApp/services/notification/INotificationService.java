package com.sena.BogotaMetroApp.services.notification;

public interface INotificationService {

    // Para mensajes a todos (Interrupciones)
    void enviarBroadcast(String destino, Object payload);

    // Para mensajes a grupos específicos (Salas de Chat)
    void enviarAGrupo(String destino, String idGrupo, Object payload);

    // Para mensajes a un usuario específico (Notificaciones personales)
    void enviarAUsuario(String username, String destino, Object payload);
}
