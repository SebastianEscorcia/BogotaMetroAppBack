package com.sena.BogotaMetroApp.configuration.security;

import com.sena.BogotaMetroApp.utils.SecurityConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))  // Usa el bean CorsConfigurationSource automáticamente // Customizer.withDefaults()
                .authorizeHttpRequests(auth -> auth
                        // 1. Rutas Públicas (Login, Registro, WebSockets)
                        .requestMatchers(SecurityConstant.PUBLIC_URLS).permitAll()
                        // 2. Logout siempre accesible (aunque el token haya expirado)
                        .requestMatchers("/api/auth/**", "/ws-metro/**").permitAll()
                        // 3. Rutas específicas por rol
                        .requestMatchers("/api/pasajero-viaje/**").hasAnyRole("SOPORTE", "OPERADOR")
                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                        // 4. Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
