package com.sena.BogotaMetroApp.services.notificationwebsocket.transaccion;

/**
 * Interfaz que define el contrato para notificar transacciones a los usuarios.
 * Sigue el patrón Adapter para desacoplar la lógica de notificación del mecanismo de transporte (WebSocket).
 */
public interface ITransaccionNotifier {
    /**
     * Notifica al usuario que su recarga fue exitosa.
     *
     * @param username El correo/username del usuario a notificar
     * @param payload  Los datos de la recarga a enviar
     */
    void notificarRecargaExitosa(String username, Object payload);
}
