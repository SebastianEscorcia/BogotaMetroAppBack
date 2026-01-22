# 🏗️ Arquitectura de Caché Redis - BogotaMetroApp

Documentación técnica de la arquitectura de caché distribuida implementada con Redis en el sistema BogotaMetroApp.

---

## 📋 Tabla de Contenidos

- [Visión General](#-visión-general)
- [Arquitectura](#-arquitectura)
- [AbstractRedisCacheService](#-abstractrediscacheservice)
- [Implementaciones Concretas](#-implementaciones-concretas)
- [Patrones de Diseño](#-patrones-de-diseño)
- [Estrategias de Caché](#-estrategias-de-caché)
- [Configuración](#-configuración)
- [Mejores Prácticas](#-mejores-prácticas)
- [Monitoreo y Métricas](#-monitoreo-y-métricas)

---

## 🎯 Visión General

La arquitectura de caché de BogotaMetroApp está diseñada para:

- ✅ **Reducir latencia**: Respuestas en < 15ms vs 150-300ms sin caché
- ✅ **Reducir carga en DB**: Hasta 95% menos consultas a MySQL
- ✅ **Escalabilidad**: Arquitectura distribuida y horizontal
- ✅ **Consistencia**: Invalidación automática en escrituras
- ✅ **Reutilización**: Clase base abstracta para todos los servicios
- ✅ **Mantenibilidad**: Código centralizado y fácil de extender

---

## 🏛️ Arquitectura

### Diagrama de Arquitectura General

```
┌─────────────────────────────────────────────────────────────────┐
│                         APPLICATION LAYER                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │ QrController │  │CategoryFaq   │  │SupportFaq    │          │
│  │              │  │Controller    │  │Controller    │          │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘          │
└─────────┼──────────────────┼──────────────────┼─────────────────┘
          │                  │                  │
          ▼                  ▼                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                         SERVICE LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │QrServiceImpl │  │CategoryFaq   │  │SupportFaq    │          │
│  │              │  │ServiceImpl   │  │ServiceImpl   │          │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘          │
└─────────┼──────────────────┼──────────────────┼─────────────────┘
          │                  │                  │
          ▼                  ▼                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                       CACHE SERVICE LAYER                        │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │           AbstractRedisCacheService (Abstract)           │   │
│  │  • buildKey(String... parts)                             │   │
│  │  • setWithExpiry(key, value, duration)                   │   │
│  │  • get(key, type)                                        │   │
│  │  • delete(key)                                           │   │
│  │  • exists(key)                                           │   │
│  └────────────────────────┬─────────────────────────────────┘   │
│                           │                                      │
│         ┌─────────────────┼─────────────────┐                  │
│         │                 │                 │                  │
│         ▼                 ▼                 ▼                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐        │
│  │ QrRedisCache │  │CategoryFaq   │  │FaqRedis      │        │
│  │ ServiceImpl  │  │CacheService  │  │CacheService  │        │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘        │
└─────────┼──────────────────┼──────────────────┼───────────────┘
          │                  │                  │
          └──────────────────┼──────────────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │  RedisTemplate  │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │  Redis Server   │
                    │  (Port 6379)    │
                    └─────────────────┘
```

### Flujo de Datos

```
┌─────────────────────────────────────────────────────────────┐
│ 1. Cliente solicita datos                                   │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│ 2. Service verifica cache (getCached...)                    │
└────────────┬────────────────────────────────────────────────┘
             │
    ┌────────┴────────┐
    │                 │
    ▼                 ▼
 CACHE HIT        CACHE MISS
    │                 │
    │                 ▼
    │      ┌─────────────────────────────────┐
    │      │ 3. Consultar MySQL              │
    │      └──────────┬──────────────────────┘
    │                 │
    │                 ▼
    │      ┌─────────────────────────────────┐
    │      │ 4. Guardar en cache (cache...)  │
    │      └──────────┬──────────────────────┘
    │                 │
    └────────┬────────┘
             │
             ▼
┌─────────────────────────────────────────────────────────────┐
│ 5. Retornar datos al cliente                                │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│ WRITE OPERATIONS (POST/PUT/DELETE)                          │
│ • Ejecutar operación en MySQL                               │
│ • Invalidar cache (invalidate...)                           │
│ • Próximas lecturas recargarán cache                        │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎨 AbstractRedisCacheService

### Propósito

Clase base abstracta que proporciona funcionalidad común de caché para todos los servicios Redis, siguiendo el principio DRY (Don't Repeat Yourself).

### Código Completo

```java
package com.sena.BogotaMetroApp.externalservices.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

/**
 * Clase base abstracta para servicios de caché Redis.
 * Proporciona métodos comunes para todas las implementaciones de caché.
 * 
 * @author BogotaMetroApp Team
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractRedisCacheService {
    
    protected final RedisTemplate<String, Object> redisTemplate;
    
    /**
     * Construye una clave de Redis concatenando las partes con ":"
     * 
     * @param parts Partes de la clave
     * @return Clave formateada (ej: "usuario:1:qr")
     */
    protected String buildKey(String... parts) {
        return String.join(":", parts);
    }
    
    /**
     * Guarda un valor en Redis con tiempo de expiración
     * 
     * @param key Clave de Redis
     * @param value Valor a guardar
     * @param duration Duración del TTL
     */
    protected <T> void setWithExpiry(String key, T value, Duration duration) {
        try {
            redisTemplate.opsForValue().set(key, value, duration);
            log.debug("Valor guardado en Redis: key={}, TTL={}s", key, duration.getSeconds());
        } catch (Exception e) {
            log.error("Error guardando en Redis: key={}", key, e);
        }
    }
    
    /**
     * Obtiene un valor de Redis
     * 
     * @param key Clave de Redis
     * @param type Clase del tipo esperado
     * @return Optional con el valor si existe
     */
    protected <T> Optional<T> get(String key, Class<T> type) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                log.debug("Valor obtenido de Redis: key={}", key);
                return Optional.of(type.cast(value));
            }
        } catch (Exception e) {
            log.error("Error obteniendo de Redis: key={}", key, e);
        }
        return Optional.empty();
    }
    
    /**
     * Elimina una clave de Redis
     * 
     * @param key Clave a eliminar
     */
    protected void delete(String key) {
        try {
            Boolean deleted = redisTemplate.delete(key);
            log.debug("Clave eliminada de Redis: key={}, success={}", key, deleted);
        } catch (Exception e) {
            log.error("Error eliminando de Redis: key={}", key, e);
        }
    }
    
    /**
     * Elimina múltiples claves de Redis
     * 
     * @param keys Set de claves a eliminar
     */
    protected void deleteMultiple(Set<String> keys) {
        try {
            if (keys != null && !keys.isEmpty()) {
                Long deleted = redisTemplate.delete(keys);
                log.debug("{} claves eliminadas de Redis", deleted);
            }
        } catch (Exception e) {
            log.error("Error eliminando múltiples claves de Redis", e);
        }
    }
    
    /**
     * Verifica si una clave existe en Redis
     * 
     * @param key Clave a verificar
     * @return true si existe, false si no
     */
    protected boolean exists(String key) {
        try {
            Boolean exists = redisTemplate.hasKey(key);
            return exists != null && exists;
        } catch (Exception e) {
            log.error("Error verificando existencia en Redis: key={}", key, e);
            return false;
        }
    }
    
    /**
     * Obtiene el TTL de una clave
     * 
     * @param key Clave
     * @return TTL en segundos, -1 si no tiene expiración, -2 si no existe
     */
    protected Long getTTL(String key) {
        try {
            return redisTemplate.getExpire(key);
        } catch (Exception e) {
            log.error("Error obteniendo TTL de Redis: key={}", key, e);
            return -2L;
        }
    }
}
```

### Métodos Principales

| Método | Descripción | Uso |
|--------|-------------|-----|
| `buildKey(String...)` | Construye claves consistentes | `buildKey("usuario", "1", "qr")` → `"usuario:1:qr"` |
| `setWithExpiry(key, value, duration)` | Guarda con TTL | Cache temporal |
| `get(key, type)` | Obtiene valor tipado | Lectura de cache |
| `delete(key)` | Elimina una clave | Invalidación |
| `deleteMultiple(keys)` | Elimina múltiples claves | Invalidación masiva |
| `exists(key)` | Verifica existencia | Validaciones |
| `getTTL(key)` | Obtiene tiempo restante | Monitoreo |

---

## 🔧 Implementaciones Concretas

### 1. QrRedisQrCacheServiceImpl

**Propósito**: Gestión de caché de códigos QR con versionamiento optimista.

**Keys Gestionadas**:
- `usuario:{id}:qr` - QR activo por usuario (TTL dinámico)
- `qr:{codigo}` - QR por código (TTL dinámico)

**Características Especiales**:
- Versionamiento optimista con scripts Lua
- TTL dinámico basado en expiración del QR
- Guardado dual (por usuario y por código)

```java
@Service
@Slf4j
public class QrRedisQrCacheServiceImpl extends AbstractRedisCacheService 
        implements IQrCacheService {
    
    private static final String USER_QR_PREFIX = "usuario";
    private static final String QR_PREFIX = "qr";
    
    @Override
    public void saveQrInCache(QrCacheDTO qrCacheDTO, Long ttlSeconds) {
        String userKey = buildKey(USER_QR_PREFIX, qrCacheDTO.getUsuarioId().toString(), "qr");
        String qrKey = buildKey(QR_PREFIX, qrCacheDTO.getCodigo());
        Duration duration = Duration.ofSeconds(ttlSeconds);
        
        // Guardar con versionamiento optimista (script Lua)
        saveQrWithVersioning(userKey, qrCacheDTO, duration);
        
        // Guardar también por código
        setWithExpiry(qrKey, qrCacheDTO, duration);
    }
}
```

---

### 2. CategoryFaqRedisCacheServiceImpl

**Propósito**: Caché de categorías de FAQs.

**Keys Gestionadas**:
- `categoryfaqs:all` - Lista completa de categorías activas (TTL: 24h)

**Características Especiales**:
- Cache simple de lista completa
- Invalidación total en cualquier cambio
- TTL largo (24 horas)

```java
@Service
@Slf4j
public class CategoryFaqRedisCacheServiceImpl extends AbstractRedisCacheService 
        implements ICategoryFaqCacheService {
    
    private static final String CATEGORY_FAQ_KEY = "categoryfaqs:all";
    private static final Duration CACHE_TTL = Duration.ofHours(24);
    
    @Override
    public void cacheCategoryFaqs(List<CategoryFaqResponseDTO> categories) {
        setWithExpiry(CATEGORY_FAQ_KEY, categories, CACHE_TTL);
        log.info("💾 Categorías guardadas en cache - Total: {}", categories.size());
    }
    
    @Override
    public void invalidateCategoryFaqsCache() {
        delete(CATEGORY_FAQ_KEY);
        log.info("🗑️ Cache de categorías invalidado");
    }
}
```

---

### 3. FaqRedisCacheServiceImpl

**Propósito**: Caché multi-nivel de FAQs (global y por categoría).

**Keys Gestionadas**:
- `supportfaqs:all` - Todas las FAQs activas (TTL: 24h)
- `supportfaqs:category:{id}` - FAQs por categoría (TTL: 12h)

**Características Especiales**:
- Caché de dos niveles
- Invalidación en cascada (elimina todos los niveles)
- TTL diferenciado por nivel

```java
@Service
@Slf4j
public class FaqRedisCacheServiceImpl extends AbstractRedisCacheService 
        implements ISupportFaqCacheService {
    
    private static final String FAQ_ALL_KEY = "supportfaqs:all";
    private static final String FAQ_CATEGORY_PREFIX = "supportfaqs:category";
    
    @Override
    public void invalidateSupportFaqsCache() {
        // Invalidar cache global
        delete(FAQ_ALL_KEY);
        
        // Invalidar todos los caches por categoría
        Set<String> keys = redisTemplate.keys(FAQ_CATEGORY_PREFIX + ":*");
        if (keys != null && !keys.isEmpty()) {
            deleteMultiple(keys);
            log.info("🗑️ Cache de FAQs invalidado - {} keys eliminadas", keys.size() + 1);
        }
    }
}
```

---

## 🎯 Patrones de Diseño

### 1. Template Method Pattern

La clase `AbstractRedisCacheService` implementa el patrón Template Method, definiendo la estructura común mientras permite que las subclases personalicen comportamientos específicos.

```
AbstractRedisCacheService (Template)
         │
         ├─ buildKey()        ← Método común
         ├─ setWithExpiry()   ← Método común
         ├─ get()             ← Método común
         └─ delete()          ← Método común
         
Implementaciones concretas:
         │
         ├─ QrRedisQrCacheServiceImpl
         │   └─ saveQrWithVersioning()  ← Método específico
         │
         ├─ CategoryFaqRedisCacheServiceImpl
         │   └─ cacheCategoryFaqs()     ← Método específico
         │
         └─ FaqRedisCacheServiceImpl
             └─ invalidateSupportFaqsCache()  ← Método específico
```

### 2. Strategy Pattern

Cada implementación de caché define su propia estrategia de almacenamiento y expiración:

- **QR**: TTL dinámico basado en expiración del QR
- **CategoryFaq**: TTL fijo de 24 horas
- **SupportFaq**: TTL diferenciado (24h global, 12h por categoría)

### 3. Cache-Aside Pattern

```
1. Aplicación intenta leer del cache
2. Si MISS → Lee de DB → Guarda en cache
3. Si HIT → Retorna del cache
4. En escrituras → Invalida cache
```

---

## 📊 Estrategias de Caché

### Comparación de Estrategias

| Sistema | Estrategia | TTL | Invalidación | Uso |
|---------|-----------|-----|--------------|-----|
| **QR** | Cache-Aside + TTL Dinámico | Variable (1-5 min) | Por usuario + código | Alta frecuencia, datos temporales |
| **CategoryFaq** | Cache-Aside + Write-Through | 24 horas | Total | Baja frecuencia de cambios |
| **SupportFaq** | Cache-Aside Multi-nivel | 24h global, 12h categoría | En cascada | Lectura intensiva, escritura baja |

### Cuándo Usar Cada Estrategia

**TTL Dinámico (QR)**:
- ✅ Datos con vida útil conocida
- ✅ Sincronización natural con expiración
- ❌ Overhead de cálculo de TTL

**TTL Fijo Largo (CategoryFaq)**:
- ✅ Datos que cambian poco
- ✅ Simple de implementar
- ❌ Puede retornar datos desactualizados hasta 24h

**Multi-nivel (SupportFaq)**:
- ✅ Optimiza consultas comunes (por categoría)
- ✅ Reduce cache misses
- ❌ Invalidación más compleja

---

## ⚙️ Configuración

### application.properties

```properties
# Redis Configuration
spring.data.redis.host=${REDIS_HOST:localhost}
spring.data.redis.port=${REDIS_PORT:6379}
spring.data.redis.password=${REDIS_PASSWORD:}
spring.data.redis.timeout=60000
spring.data.redis.database=0

# Connection Pool (Lettuce)
spring.data.redis.lettuce.pool.max-active=8
spring.data.redis.lettuce.pool.max-idle=8
spring.data.redis.lettuce.pool.min-idle=2
spring.data.redis.lettuce.pool.max-wait=-1ms

# Serialization
spring.data.redis.repositories.enabled=false
```

### RedisConfig.java

```java
@Configuration
@EnableCaching
public class RedisConfig {
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory) {
        
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // Serialización JSON para valores
        Jackson2JsonRedisSerializer<Object> serializer = 
            new Jackson2JsonRedisSerializer<>(Object.class);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(
            mapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL
        );
        serializer.setObjectMapper(mapper);
        
        // Configurar serializadores
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        
        template.afterPropertiesSet();
        return template;
    }
    
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1))
            .disableCachingNullValues();
        
        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .build();
    }
}
```

### Docker Compose

```yaml
version: '3.8'

services:
  redis-metro:
    image: redis:7-alpine
    container_name: redis-metro
    ports:
      - "${REDIS_PORT:6379}:6379"
    command: >
      redis-server 
      --requirepass "${REDIS_PASSWORD}"
      --notify-keyspace-events Ex
      --maxmemory 256mb
      --maxmemory-policy allkeys-lru
    volumes:
      - redis-data:/data
    restart: always
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 10s
      timeout: 3s
      retries: 3
    networks:
      - metro-network

volumes:
  redis-data:

networks:
  metro-network:
    driver: bridge
```

---

## 📚 Mejores Prácticas

### 1. Nomenclatura de Keys

✅ **Buenas prácticas:**
```
usuario:1:qr              # Jerárquico, claro
supportfaqs:category:5    # Prefijo común, fácil de buscar
categoryfaqs:all          # Descriptivo
```

❌ **Malas prácticas:**
```
user1qr                   # No jerárquico
faq_5                     # Ambiguo
data                      # Demasiado genérico
```

### 2. TTL Apropiado

```java
// ✅ TTL basado en la naturaleza de los datos
Duration.ofSeconds(ttlSeconds);  // QR: dinámico
Duration.ofHours(24);            // CategoryFaq: cambios poco frecuentes
Duration.ofHours(12);            // SupportFaq por categoría: medio

// ❌ TTL arbitrario
Duration.ofMinutes(5);           // Sin justificación
```

### 3. Invalidación Inteligente

```java
// ✅ Invalidar todos los niveles afectados
@Override
public void invalidateSupportFaqsCache() {
    delete(FAQ_ALL_KEY);
    Set<String> keys = redisTemplate.keys(FAQ_CATEGORY_PREFIX + ":*");
    deleteMultiple(keys);
}

// ❌ Invalidación parcial
@Override
public void invalidateSupportFaqsCache() {
    delete(FAQ_ALL_KEY);  // Olvida los caches por categoría
}
```

### 4. Manejo de Errores

```java
// ✅ Try-catch con fallback
try {
    Object cached = redisTemplate.opsForValue().get(key);
    if (cached != null) return Optional.of(cached);
} catch (Exception e) {
    log.error("Error en Redis, fallback a DB", e);
}
return Optional.empty();  // Permite consultar DB

// ❌ Sin manejo de errores
Object cached = redisTemplate.opsForValue().get(key);  // Puede lanzar excepción
```

### 5. Logging Apropiado

```java
// ✅ Logs informativos y útiles
log.info("✅ Cache HIT - FAQs obtenidas desde Redis (global)");
log.info("💾 FAQs guardadas en cache - Total: {}", faqs.size());
log.info("🗑️ Cache de FAQs invalidado - {} keys eliminadas", keys.size());

// ❌ Logs poco útiles
log.info("Cache hit");
log.info("Saved");
```

---

## 📈 Monitoreo y Métricas

### Comandos Redis CLI

```bash
# Ver todas las claves de un patrón
KEYS supportfaqs:*

# Ver información de una clave
TYPE usuario:1:qr
TTL usuario:1:qr
GET usuario:1:qr

# Estadísticas del servidor
INFO stats
INFO memory

# Monitorear comandos en tiempo real
MONITOR

# Ver clientes conectados
CLIENT LIST

# Limpiar base de datos específica
SELECT 0
FLUSHDB

# Limpiar todas las bases de datos
FLUSHALL
```

### Métricas Importantes

1. **Hit Rate**: `(cache_hits / (cache_hits + cache_misses)) * 100`
2. **Memoria Usada**: `used_memory_human`
3. **Clientes Conectados**: `connected_clients`
4. **Comandos por Segundo**: `instantaneous_ops_per_sec`
5. **Keys Expiradas**: `expired_keys`

### Implementar Métricas con Micrometer

```java
@Service
@Slf4j
public class CacheMetricsService {
    
    private final MeterRegistry meterRegistry;
    
    public CacheMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    
    public void recordCacheHit(String cacheName) {
        meterRegistry.counter("cache.hits", "cache", cacheName).increment();
    }
    
    public void recordCacheMiss(String cacheName) {
        meterRegistry.counter("cache.misses", "cache", cacheName).increment();
    }
    
    public void recordCacheSize(String cacheName, long size) {
        meterRegistry.gauge("cache.size", Tags.of("cache", cacheName), size);
    }
}
```

---

## 🚀 Escalabilidad

### Redis Cluster (Futuro)

Para escalar horizontalmente:

```yaml
version: '3.8'

services:
  redis-node-1:
    image: redis:7-alpine
    command: redis-server --cluster-enabled yes --cluster-config-file nodes.conf
    ports:
      - "7001:6379"
  
  redis-node-2:
    image: redis:7-alpine
    command: redis-server --cluster-enabled yes --cluster-config-file nodes.conf
    ports:
      - "7002:6379"
  
  redis-node-3:
    image: redis:7-alpine
    command: redis-server --cluster-enabled yes --cluster-config-file nodes.conf
    ports:
      - "7003:6379"
```

### Redis Sentinel (Alta Disponibilidad)

Para failover automático:

```yaml
version: '3.8'

services:
  redis-master:
    image: redis:7-alpine
    ports:
      - "6379:6379"
  
  redis-slave:
    image: redis:7-alpine
    command: redis-server --slaveof redis-master 6379
  
  redis-sentinel:
    image: redis:7-alpine
    command: redis-sentinel /etc/redis/sentinel.conf
```

---

## 📚 Documentación Relacionada

- [Sistema de QR](README-QR-System.md)
- [Sistema de Categorías FAQ](./README-CategoryFaq-System.md)
- [Sistema de Support FAQ](./README-SupportFaq-System.md)

---

## 👥 Contribuidores

- **Arquitectura y Desarrollo**: Sebastian David Escorcia Montes
- **Colaboradores**: Jeniffer Saumeth

---

## 📄 Licencia

Este proyecto es parte del sistema BogotaMetroApp - SENA 2025
