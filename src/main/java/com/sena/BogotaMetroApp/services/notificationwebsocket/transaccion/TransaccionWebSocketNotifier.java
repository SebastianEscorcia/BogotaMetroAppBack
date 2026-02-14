package com.sena.BogotaMetroApp.services.notificationwebsocket.transaccion;

import com.sena.BogotaMetroApp.services.notificationwebsocket.IUserNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

/**
 * Adaptador que implementa la notificación de transacciones vía WebSocket.
 * Usa IUserNotifier para enviar mensajes al usuario específico.
 *
 * Este servicio sigue el patrón Adapter, permitiendo desacoplar la lógica
 * de notificación del mecanismo de transporte (WebSocket).
 *
 * @author Sebastian Escorcia
 * @version 1.1
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransaccionWebSocketNotifier implements ITransaccionNotifier {

    private final IUserNotifier userNotifier;
    private final SimpUserRegistry simpUserRegistry;
    @Override
    public void notificarRecargaExitosa(String username, Object payload) {
        log.info("Sesiones STOMP activas: {}",
                simpUserRegistry.getUsers().stream()
                        .map(u -> u.getName() + " -> " + u.getSessions().size())
                        .toList()
        );
        userNotifier.enviarAUsuario(username, payload);
    }
}
