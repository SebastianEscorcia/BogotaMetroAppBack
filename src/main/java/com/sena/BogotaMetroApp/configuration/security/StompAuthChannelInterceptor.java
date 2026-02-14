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

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompAuthChannelInterceptor implements ChannelInterceptor {
    private final UsuarioRepository usuarioRepository;

    @Override
    public @Nullable Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            Map<String, Object> attrs = accessor.getSessionAttributes();
            String correo = attrs != null ? (String) attrs.get("ws_user_email") : null;

            if (correo == null || correo.isBlank()) {
                log.warn("CONNECT rechazado: no hay usuario en atributos de handshake");
                try {
                    throw new AccessDeniedException("No autorizado para WebSocket");
                } catch (AccessDeniedException e) {
                    throw new RuntimeException(e);
                }
            }

            Usuario usuario = null;
            try {
                usuario = usuarioRepository.findByCorreo(correo)
                        .orElseThrow(() -> new AccessDeniedException("Usuario no encontrado"));
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }

            String rol = usuario.getRol().getNombre();
            if (!"PASAJERO".equalsIgnoreCase(rol)) {
                log.warn("CONNECT rechazado para {} con rol {}", correo, rol);
                try {
                    throw new AccessDeniedException("Rol no autorizado para este canal");
                } catch (AccessDeniedException e) {
                    throw new RuntimeException(e);
                }
            }

            var auth = new UsernamePasswordAuthenticationToken(
                    usuario.getCorreo(),
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + rol))
            );

            accessor.setUser(auth);
            log.info("STOMP user autenticado: {}", auth.getName());
        }

        return message;
    }

}
