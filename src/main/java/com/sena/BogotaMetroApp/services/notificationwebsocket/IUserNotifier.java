package com.sena.BogotaMetroApp.services.notificationwebsocket;

public interface IUserNotifier {
    void enviarAUsuario(String username, Object payload);
}
