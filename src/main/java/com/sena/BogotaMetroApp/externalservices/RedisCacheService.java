package com.sena.BogotaMetroApp.externalservices;

import com.sena.BogotaMetroApp.presentation.dto.QrCacheDTO;
import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String USER_QR_KEY = "usuario:%d:qr";
    private static final String QR_KEY = "qr:%s";

    // Script Lua para realizar un HMSET condicional basado en la versión para 8 campos (16 argumentos field/value + 1 version)
    private static final String LUA_CAS_HMSET =
            "local existing = redis.call('HGET', KEYS[1], 'version') " +
                    "if (not existing) or (tonumber(ARGV[1]) > tonumber(existing)) then " +
                    "  redis.call('HMSET', KEYS[1], " +
                    "    ARGV[2], ARGV[3], ARGV[4], ARGV[5], ARGV[6], ARGV[7], ARGV[8], ARGV[9], " +
                    "    ARGV[10], ARGV[11], ARGV[12], ARGV[13], ARGV[14], ARGV[15], ARGV[16], ARGV[17]) " +
                    "  return 1 " +
                    "end " +
                    "return 0";

    private final DefaultRedisScript<Long> casScript = createScript();

    private DefaultRedisScript<Long> createScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(LUA_CAS_HMSET);
        script.setResultType(Long.class);
        return script;
    }

    private String userKey(Long usuarioId) {
        return String.format(USER_QR_KEY, usuarioId);
    }

    private String qrKey(String codigo) {
        return String.format(QR_KEY, codigo);
    }

    public void cacheQr(QrCacheDTO dto) {
        if (dto == null || dto.getFechaExpiracion() == null) {
            log.warn("No se puede cachear QR: dto es null o fechaExpiracion es null");
            return;
        }

        long ttl = Duration.between(LocalDateTime.now(), dto.getFechaExpiracion()).getSeconds();
        log.info("Intentando cachear QR - usuarioId: {}, codigo: {}, ttl: {} segundos",
                dto.getUsuarioId(), dto.getContenido(), ttl);

        if (ttl <= 0) {
            log.warn("TTL <= 0, no se cachea el QR");
            return;
        }

        long incomingVersion = Math.max(1, dto.getVersion());

        List<String> args = Arrays.asList(
                String.valueOf(incomingVersion),
                "id", dto.getId() == null ? "" : dto.getId().toString(),
                "usuarioId", dto.getUsuarioId() == null ? "" : dto.getUsuarioId().toString(),
                "codigo", dto.getContenido() == null ? "" : dto.getContenido(),
                "tipo", dto.getTipoQr() == null ? "" : dto.getTipoQr().name(),
                "fechaGeneracion", dto.getFechaGeneracion() == null ? "" : dto.getFechaGeneracion().toString(),
                "fechaExpiracion", dto.getFechaExpiracion().toString(),
                "consumido", String.valueOf(dto.isConsumido()),
                "version", String.valueOf(incomingVersion)
        );

        try {
            String userKey = userKey(dto.getUsuarioId());
            Long resUser = redisTemplate.execute(casScript, Collections.singletonList(userKey), (Object[]) args.toArray(new String[0]));
            log.info("Resultado cache userKey [{}]: {}", userKey, resUser);

            if (resUser != null && resUser == 1L) {
                redisTemplate.expire(userKey, ttl, TimeUnit.SECONDS);
                log.info("TTL aplicado a userKey: {} segundos", ttl);
            }

            String qrKey = qrKey(dto.getContenido());
            Long resQr = redisTemplate.execute(casScript, Collections.singletonList(qrKey), (Object[]) args.toArray(new String[0]));
            log.info("Resultado cache qrKey [{}]: {}", qrKey, resQr);

            if (resQr != null && resQr == 1L) {
                redisTemplate.expire(qrKey, ttl, TimeUnit.SECONDS);
                log.info("TTL aplicado a qrKey: {} segundos", ttl);
            }

        } catch (Exception e) {
            log.error("Error cacheando QR en Redis: {}", e.getMessage(), e);
        }
    }

    public QrCacheDTO getQrUsuario(Long usuarioId) {
        try {
            String key = userKey(usuarioId);
            log.info("Buscando en Redis key: {}", key);

            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

            if (entries == null || entries.isEmpty()) {
                log.info("No se encontró cache para key: {}", key);
                return null;
            }

            log.info("Cache encontrado para key: {}, entries: {}", key, entries);
            return mapFromHash(entries);
        } catch (Exception e) {
            log.error("Error leyendo cache de Redis: {}", e.getMessage(), e);
            return null;
        }
    }

    // ...existing code para los demás métodos...

    public QrCacheDTO getQrByCodigo(String codigo) {
        try {
            String key = qrKey(codigo);
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
            if (entries == null || entries.isEmpty()) return null;
            return mapFromHash(entries);
        } catch (Exception e) {
            return null;
        }
    }

    public void invalidateByUsuarioId(Long usuarioId) {
        try {
            String key = userKey(usuarioId);
            Boolean deleted = redisTemplate.delete(key);
            log.info("Invalidando cache userKey [{}]: {}", key, deleted);
        } catch (Exception e) {
            log.error("Error invalidando cache: {}", e.getMessage());
        }
    }

    public void invalidateByCodigo(String codigo) {
        try {
            String key = qrKey(codigo);
            Boolean deleted = redisTemplate.delete(key);
            log.info("Invalidando cache qrKey [{}]: {}", key, deleted);
        } catch (Exception e) {
            log.error("Error invalidando cache: {}", e.getMessage());
        }
    }

    public void invalidate(QrCacheDTO dto) {
        if (dto == null) return;
        invalidateByUsuarioId(dto.getUsuarioId());
        if (dto.getContenido() != null) invalidateByCodigo(dto.getContenido());
    }

    private QrCacheDTO mapFromHash(Map<Object, Object> h) {
        QrCacheDTO dto = new QrCacheDTO();

        Object idObj = h.get("id");
        if (idObj != null && !idObj.toString().isEmpty()) {
            dto.setId(Long.valueOf(idObj.toString()));
        }

        Object usuarioIdObj = h.get("usuarioId");
        if (usuarioIdObj != null && !usuarioIdObj.toString().isEmpty()) {
            dto.setUsuarioId(Long.valueOf(usuarioIdObj.toString()));
        }

        Object codigoObj = h.get("codigo");
        if (codigoObj != null && !codigoObj.toString().isEmpty()) {
            dto.setContenido(codigoObj.toString());
        }

        Object tipoObj = h.get("tipo");
        if (tipoObj != null && !tipoObj.toString().isEmpty()) {
            dto.setTipoQr(TipoQr.valueOf(tipoObj.toString()));
        }

        Object fechaGenObj = h.get("fechaGeneracion");
        if (fechaGenObj != null && !fechaGenObj.toString().isEmpty()) {
            dto.setFechaGeneracion(LocalDateTime.parse(fechaGenObj.toString()));
        }

        Object fechaExpObj = h.get("fechaExpiracion");
        if (fechaExpObj != null && !fechaExpObj.toString().isEmpty()) {
            dto.setFechaExpiracion(LocalDateTime.parse(fechaExpObj.toString()));
        }

        Object consumidoObj = h.get("consumido");
        if (consumidoObj != null) {
            dto.setConsumido(Boolean.parseBoolean(consumidoObj.toString()));
        }

        Object versionObj = h.get("version");
        if (versionObj != null && !versionObj.toString().isEmpty()) {
            dto.setVersion(Long.parseLong(versionObj.toString()));
        }

        return dto;
    }
}