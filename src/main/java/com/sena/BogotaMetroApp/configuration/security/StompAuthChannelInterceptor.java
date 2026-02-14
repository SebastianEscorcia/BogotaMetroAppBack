package com.sena.BogotaMetroApp.configuration.security;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Interceptor STOMP para autenticar usuarios en conexiones WebSocket.
 * Este interceptor valida que el usuario esté autenticado durante el handshake
 * y establece el contexto de seguridad para la sesión WebSocket.
 * Permite conexión a CUALQUIER usuario autenticado (PASAJERO, ADMIN, SOPORTE, etc.)
 * La autorización específica por rol se maneja en los controladores de mensajes.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final UsuarioRepository usuarioRepository;

    @Override
    public @Nullable Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || !StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        // Obtener el correo del usuario desde los atributos del handshake
        Map<String, Object> attrs = accessor.getSessionAttributes();
        String correo = (attrs != null) ? (String) attrs.get("ws_user_email") : null;

        if (correo == null || correo.isBlank()) {
            log.warn("STOMP CONNECT rechazado: no hay usuario autenticado en el handshake");
            throw new IllegalStateException("No autorizado para WebSocket: usuario no autenticado");
        }

        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);

        if (usuario == null) {
            log.warn("STOMP CONNECT rechazado: usuario no encontrado en BD - {}", correo);
            throw new IllegalStateException("No autorizado para WebSocket: usuario no existe");
        }

        if (!usuario.isActivo()) {
            log.warn("STOMP CONNECT rechazado: usuario inactivo - {}", correo);
            throw new IllegalStateException("No autorizado para WebSocket: usuario inactivo");
        }

        // Crear la autenticación con el rol del usuario
        String rol = usuario.getRol().getNombre();
        var auth = new UsernamePasswordAuthenticationToken(
                usuario.getCorreo(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + rol))
        );

        // Establecer el usuario autenticado en la sesión STOMP
        accessor.setUser(auth);
        log.info("STOMP usuario autenticado: {} con rol {}", correo, rol);

        return message;
    }

}
