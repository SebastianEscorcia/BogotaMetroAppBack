package com.sena.BogotaMetroApp.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable).cors(cors -> cors.configure(http))
                .authorizeHttpRequests(auth -> auth
                        // 1. Rutas Públicas (Login, Registro, WebSockets)
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/pasajero/registro").permitAll()
                        .requestMatchers("api/roles/**").permitAll()
                        .requestMatchers("/api/rutas/**").hasRole("OPERADOR")
                        .requestMatchers("/api/viajes/**").hasRole("OPERADOR")
                        .requestMatchers(HttpMethod.POST, "/api/soporte").hasRole("OPERADOR")
                        .requestMatchers("/api/soporte/**").hasAnyRole("SOPORTE", "OPERADOR")
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                        //.requestMatchers("/api/operador/**").hasRole("OPERADOR")
                        .requestMatchers("/api/operador/**").permitAll()
                        //CHATS
                        .requestMatchers("/ws-metro/**").permitAll() // Permitir handshake inicial

                        //.requestMatchers("/api/chat-rooms/**").hasAnyRole("PASAJERO", "SOPORTE")
                        .requestMatchers("/api/chat-rooms/**").permitAll()
                        .requestMatchers("/api/pasajero/**").hasRole("PASAJERO")
                        //.requestMatchers("/api/soporte/**").hasRole("SOPORTE")
                        .requestMatchers("/api/soporte/**").permitAll()
                        //Reglas para Interrupciones
                        .requestMatchers(HttpMethod.GET, "/api/interrupciones").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/interrupciones").hasRole("OPERADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/interrupciones/**").hasRole("OPERADOR")

                        .requestMatchers("/api/pasajero-viaje/**").hasAnyRole("SOPORTE", "OPERADOR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
