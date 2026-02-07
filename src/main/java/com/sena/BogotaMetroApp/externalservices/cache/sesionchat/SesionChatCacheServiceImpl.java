package com.sena.BogotaMetroApp.externalservices.cache.sesionchat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sena.BogotaMetroApp.externalservices.cache.AbstractRedisCacheService;
import com.sena.BogotaMetroApp.presentation.dto.sesionchat.SesionChatResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service

public class SesionChatCacheServiceImpl extends AbstractRedisCacheService implements ISesionChatCacheService {

    // Prefijos de keys
    private static final String SESIONES_PENDIENTES_KEY = "chat:sesiones:pendientes";
    private static final String SESIONES_ACTIVAS_PREFIX = "chat:sesiones:activas:soporte:";

    // TTLs
    private static final long TTL_PENDIENTES_SECONDS = 5; // 5 segundos para pendientes (compartido)
    private static final long TTL_ACTIVAS_SECONDS = 30;   // 30 segundos para activas por soporte

    protected SesionChatCacheServiceImpl(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void cachearSesionesPendientes(List<SesionChatResponseDTO> sesionesPendientes) {
        serialize(sesionesPendientes).ifPresent(json ->
                setWithTtl(SESIONES_PENDIENTES_KEY, json, TTL_PENDIENTES_SECONDS, TimeUnit.SECONDS)
        );
        log.info("📦 Cacheadas {} sesiones pendientes (TTL: {}s)", sesionesPendientes.size(), TTL_PENDIENTES_SECONDS);
    }

    @Override
    public Optional<List<SesionChatResponseDTO>> obtenerSesionesPendientesCacheadas() {
        return getValue(SESIONES_PENDIENTES_KEY).flatMap(json -> deserialize(json,  new TypeReference<List<SesionChatResponseDTO>>() {}));
    }

    @Override
    public void invalidarSesionesPendientesCache() {
        deleteKey(SESIONES_PENDIENTES_KEY);
        log.info("🗑️ Caché de sesiones pendientes invalidado");
    }

    @Override
    public void cachearSesionesActivas(Long idUsuario, List<SesionChatResponseDTO> sesionesActivas) {
        String key = SESIONES_ACTIVAS_PREFIX + idUsuario;
        serialize(sesionesActivas).ifPresent(json ->
                setWithTtl(key, json, TTL_ACTIVAS_SECONDS, TimeUnit.SECONDS)
        );
        log.debug("📦 Cacheadas {} sesiones activas para soporte {} (TTL: {}s)",
                sesionesActivas.size(), idUsuario, TTL_ACTIVAS_SECONDS);
    }

    @Override
    public Optional<List<SesionChatResponseDTO>> obtenerSesionesActivasCacheadas(Long idUsuario) {
        String key = SESIONES_ACTIVAS_PREFIX + idUsuario;
        return getValue(key)
                .flatMap(json -> deserializeList(json, new TypeReference<List<SesionChatResponseDTO>>() {}));
    }

    @Override
    public void invalidarSesionesActivasCache(Long idUsuario) {
        String key = SESIONES_ACTIVAS_PREFIX + idUsuario;
        deleteKey(key);
        log.info("🗑️ Caché de sesiones activas invalidado para soporte {}", idUsuario);
    }

    @Override
    public void invalidarTodasLasSesionesActivasCache() {
        deleteByPattern(SESIONES_ACTIVAS_PREFIX + "*");
        log.info("🗑️ Caché de todas las sesiones activas invalidado");
    }

    public void invalidarTodo() {
        invalidarSesionesPendientesCache();
        invalidarTodasLasSesionesActivasCache();
        log.info("🗑️ Todo el caché de sesiones de chat invalidado");
    }

    public void invalidarAlTomarSesion(Long idSoporte) {
        invalidarSesionesPendientesCache();
        invalidarSesionesActivasCache(idSoporte);
    }

    public void invalidarAlCerrarSesion(Long idSoporte) {
        invalidarSesionesActivasCache(idSoporte);
    }
}
