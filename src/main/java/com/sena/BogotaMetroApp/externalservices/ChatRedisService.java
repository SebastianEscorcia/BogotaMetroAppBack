package com.sena.BogotaMetroApp.externalservices;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ChatRedisService {

    private final StringRedisTemplate redisTemplate;

    @Value("${app.redis.key-prefix}")
    private String KEY_PREFIX;

    @Value("${app.redis.timeout-seconds}")
    private Long TIMEOUT_SECONDS;

    /**
     * Se llama al crear sesión(chat) o enviar mensaje.
     *  Crea o sobrescribe la llave en Redis con el tiempo de vida nuevo.
     */
    public void actualizarActividad(Long idSesion) {
        String key = KEY_PREFIX + idSesion;
        redisTemplate.opsForValue().set(key, "active", TIMEOUT_SECONDS, TimeUnit.SECONDS);
        System.out.println("Redis: Temporizador reiniciado para sesión " + idSesion);
    }

    /**
     * Elimina el temporizador de actividad en Redis.
     * Se llama al cerrar sesión (chat).
     */
    public void eliminarTemporizador(Long idSesion) {
        String key = KEY_PREFIX + idSesion;
        redisTemplate.delete(key);
    }
}
