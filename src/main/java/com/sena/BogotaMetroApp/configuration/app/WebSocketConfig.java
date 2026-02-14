package com.sena.BogotaMetroApp.configuration.app;

import com.sena.BogotaMetroApp.configuration.security.StompAuthChannelInterceptor;
import com.sena.BogotaMetroApp.configuration.security.handshake.WsJwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompAuthChannelInterceptor stompAuthChannelInterceptor;

    private final WsJwtHandshakeInterceptor wsJwtHandshakeInterceptor;
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompAuthChannelInterceptor);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Punto de conexión (Handshake). Aquí se conecta el Frontend
        registry.addEndpoint("/ws-metro").addInterceptors(wsJwtHandshakeInterceptor).setAllowedOriginPatterns("http://localhost:5173", "http://127.0.0.1:5173").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefijo para los mensajes que van DESDE el servidor HACIA el cliente
        config.enableSimpleBroker("/topic", "/queue");
        // Prefijo para los mensajes que van DESDE el cliente HACIA el servidor
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

}
