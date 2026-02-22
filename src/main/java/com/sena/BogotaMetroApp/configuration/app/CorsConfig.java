package com.sena.BogotaMetroApp.configuration.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    /**
     * Configuración de CORS usando CorsConfigurationSource (recomendado en Spring Security 6).
     * Spring Security detecta automáticamente este bean y lo aplica.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Orígenes permitidos (incluir puerto exacto)
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "http://localhost:3000",
                "http://127.0.0.1:3000",
                "http://localhost",
                "http://127.0.0.1"
        ));

        // Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS"));

        // ers permitidosHead
        config.setAllowedHeaders(List.of("*"));

        // Permitir credenciales (cookies, Authorization header)
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
