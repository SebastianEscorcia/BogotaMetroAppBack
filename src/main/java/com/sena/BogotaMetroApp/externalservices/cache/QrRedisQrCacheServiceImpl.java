package com.sena.BogotaMetroApp.externalservices.cache;

import com.sena.BogotaMetroApp.presentation.dto.QrCacheDTO;
import com.sena.BogotaMetroApp.utils.RedisCacheConstants;
import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class QrRedisQrCacheServiceImpl extends AbstractRedisCacheService  implements IQrCacheService<QrCacheDTO,Long> {


    private static final String LUA_CAS_HMSET =
            "local existing = redis.call('HGET', KEYS[1], 'version') " +
                    "if (not existing) or (tonumber(ARGV[1]) > tonumber(existing)) then " +
                    "  redis.call('HMSET', KEYS[1], " +
                    "    ARGV[2], ARGV[3], ARGV[4], ARGV[5], ARGV[6], ARGV[7], ARGV[8], ARGV[9], " +
                    "    ARGV[10], ARGV[11], ARGV[12], ARGV[13], ARGV[14], ARGV[15], ARGV[16], ARGV[17]) " +
                    "  return 1 " +
                    "end " +
                    "return 0";

    private final DefaultRedisScript<Long> casScript;

    public QrRedisQrCacheServiceImpl(RedisTemplate<String, String> redisTemplate) {
        super(redisTemplate);
        this.casScript = createScript();
    }

    private DefaultRedisScript<Long> createScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(LUA_CAS_HMSET);
        script.setResultType(Long.class);
        return script;
    }

    @Override
    public void cache(QrCacheDTO dto) {
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
        List<String> args = buildCacheArgs(dto, incomingVersion);

        try {
            cacheByUserKey(dto, args, ttl, incomingVersion);
            cacheByQrCode(dto, args, ttl);
        } catch (Exception e) {
            log.error("Error cacheando QR en Redis: {}", e.getMessage(), e);
        }
    }

    private List<String> buildCacheArgs(QrCacheDTO dto, long version) {
        return Arrays.asList(
                String.valueOf(version),
                "id", dto.getId() == null ? "" : dto.getId().toString(),
                "usuarioId", dto.getUsuarioId() == null ? "" : dto.getUsuarioId().toString(),
                "codigo", dto.getContenido() == null ? "" : dto.getContenido(),
                "tipo", dto.getTipoQr() == null ? "" : dto.getTipoQr().name(),
                "fechaGeneracion", dto.getFechaGeneracion() == null ? "" : dto.getFechaGeneracion().toString(),
                "fechaExpiracion", dto.getFechaExpiracion().toString(),
                "consumido", String.valueOf(dto.isConsumido()),
                "version", String.valueOf(version)
        );
    }

    private void cacheByUserKey(QrCacheDTO dto, List<String> args, long ttl, long version) {
        String userKey = RedisCacheConstants.userQrKey(dto.getUsuarioId());
        Long result = redisTemplate.execute(casScript, Collections.singletonList(userKey),
                (Object[]) args.toArray(new String[0]));
        log.info("Resultado cache userKey [{}]: {}", userKey, result);

        if (result == 1L) {
            redisTemplate.expire(userKey, ttl, TimeUnit.SECONDS);
            log.info("TTL aplicado a userKey: {} segundos", ttl);
        }
    }

    private void cacheByQrCode(QrCacheDTO dto, List<String> args, long ttl) {
        String qrKey = RedisCacheConstants.qrCodeKey(dto.getContenido());
        Long result = redisTemplate.execute(casScript, Collections.singletonList(qrKey),
                (Object[]) args.toArray(new String[0]));
        log.info("Resultado cache qrKey [{}]: {}", qrKey, result);

        if (result == 1L) {
            redisTemplate.expire(qrKey, ttl, TimeUnit.SECONDS);
            log.info("TTL aplicado a qrKey: {} segundos", ttl);
        }
    }

    @Override
    public Optional<QrCacheDTO> get(Long usuarioId) {
        try {
            String key = RedisCacheConstants.userQrKey(usuarioId);
            log.info("Buscando en Redis key: {}", key);

            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

            if (entries.isEmpty()) {
                log.info("No se encontró cache para key: {}", key);
                return Optional.empty();
            }

            log.info("Cache encontrado para key: {}", key);
            return Optional.of(mapFromHash(entries));
        } catch (Exception e) {
            log.error("Error leyendo cache de Redis: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Obtiene QR por código
     */
    public Optional<QrCacheDTO> getByCodigo(String codigo) {
        try {
            String key = RedisCacheConstants.qrCodeKey(codigo);
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
            if (entries.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(mapFromHash(entries));
        } catch (Exception e) {
            log.error("Error obteniendo QR por código: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void invalidate(Long usuarioId) {
        String key = RedisCacheConstants.userQrKey(usuarioId);
        deleteKey(key);
    }

    /**
     * Invalida por código
     */
    public void invalidateByCodigo(String codigo) {
        String key = RedisCacheConstants.qrCodeKey(codigo);
        deleteKey(key);
    }


    /**
     * Invalida completamente un QR (por usuario y código)
     */
    @Override
    public void invalidateFull(QrCacheDTO dto) {
        if (dto == null) return;
        invalidate(dto.getUsuarioId());
        if (dto.getContenido() != null) {
            invalidateByCodigo(dto.getContenido());
        }
    }

    @Override
    public void invalidateAll() {
        deleteByPattern("usuario:*:qr");
        deleteByPattern("qr:*");
    }

    @Override
    public boolean exists(Long usuarioId) {
        return keyExists(RedisCacheConstants.userQrKey(usuarioId));
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