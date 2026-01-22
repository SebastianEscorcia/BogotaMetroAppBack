package com.sena.BogotaMetroApp.externalservices.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AbstractRedisCacheService {
    protected final RedisTemplate<String, String> redisTemplate;
    protected final ObjectMapper objectMapper;

    protected AbstractRedisCacheService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }
    /**
     * Serializa un objeto a JSON
     */
    protected <T> Optional<String> serialize(T data) {
        try {
            return Optional.of(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            log.error("Error serializando datos: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Deserializa JSON a un objeto
     */
    protected <T> Optional<T> deserialize(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(json, clazz));
        } catch (JsonProcessingException e) {
            log.error("Error deserializando datos: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Deserializa JSON a una lista
     */
    protected <T> Optional<List<T>> deserializeList(String json, TypeReference<List<T>> typeRef) {
        if (json == null || json.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(json, typeRef));
        } catch (JsonProcessingException e) {
            log.error("Error deserializando lista: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Guarda un valor con TTL
     */
    protected void setWithTtl(String key, String value, long ttl, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl, unit);
            log.debug("Cache guardado - key: {}", key);
        } catch (Exception e) {
            log.error("Error guardando en cache key {}: {}", key, e.getMessage());
        }
    }

    /**
     * Obtiene un valor
     */
    protected Optional<String> getValue(String key) {
        try {
            String value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                log.info("✅ Cache HIT - Key: {} (longitud: {} caracteres)", key, value.length());
            } else {
                log.info("❌ Cache MISS - Key: {} no encontrada", key);
            }
            return Optional.ofNullable(value);
        } catch (Exception e) {
            log.error("❌ Error obteniendo cache key {}: {}", key, e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Elimina una key
     */
    protected boolean deleteKey(String key) {
        try {
            Boolean deleted = redisTemplate.delete(key);
            log.info("Cache invalidado - key: {}, resultado: {}", key, deleted);
            return deleted;
        } catch (Exception e) {
            log.error("Error eliminando cache key {}: {}", key, e.getMessage());
            return false;
        }
    }

    /**
     * Elimina keys por patrón
     */
    protected void deleteByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (!keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("Cache invalidado por patrón: {}, keys eliminadas: {}", pattern, keys.size());
            }
        } catch (Exception e) {
            log.error("Error eliminando cache por patrón {}: {}", pattern, e.getMessage());
        }
    }

    /**
     * Verifica si una key existe
     */
    protected boolean keyExists(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("Error verificando existencia de key {}: {}", key, e.getMessage());
            return false;
        }
    }
}
