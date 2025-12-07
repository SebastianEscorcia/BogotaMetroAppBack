package com.sena.BogotaMetroApp.configuration.app;

import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Punto de conexión (Handshake). Aquí se conecta el Frontend
        registry.addEndpoint("/ws-metro").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefijo para los mensajes que van DESDE el servidor HACIA el cliente
        config.enableSimpleBroker("/topic", "queue");
        // Prefijo para los mensajes que van DESDE el cliente HACIA el servidor
        config.setApplicationDestinationPrefixes("/app");
    }
}
