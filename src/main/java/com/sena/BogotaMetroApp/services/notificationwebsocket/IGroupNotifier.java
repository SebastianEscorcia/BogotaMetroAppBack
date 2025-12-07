package com.sena.BogotaMetroApp.services.notificationwebsocket;

public interface IGroupNotifier {
    void enviarASalaChat(Long idSala, Object payload);
}
