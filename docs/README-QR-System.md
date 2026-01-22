#  Sistema de Generación de Códigos QR con Redis Cache

Sistema de generación y validación de códigos QR para control de acceso, implementado con Spring Boot, MySQL y Redis. Utiliza una arquitectura de caché distribuida con servicios especializados que heredan de una clase base abstracta.

---

##  Tabla de Contenidos

- [Características](#-características)
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Configuración](#-configuración)
- [Flujo de Funcionamiento](#-flujo-de-funcionamiento)
- [Estructura de Archivos](#-estructura-de-archivos)
- [Arquitectura de Caché Redis](#-arquitectura-de-caché-redis)
- [API Endpoints](#-api-endpoints)
- [Caché Redis](#-caché-redis)
- [Manejo de QR Expirados](#-manejo-de-qr-expirados)
- [Solución de Problemas](#-solución-de-problemas)

---

##  Características

-  Generación de códigos QR únicos por usuario
-  Caché en Redis para respuestas ultra-rápidas con TTL automático
-  TTL automático (los QR expiran automáticamente)
-  Versionamiento optimista para evitar condiciones de carrera
-  Histórico de QR no utilizados
-  Bloqueo pesimista para operaciones concurrentes
-  Validación de QR para torniquetes
-  Arquitectura de caché escalable con `AbstractRedisCacheService`
-  Job programado para limpieza automática de QR expirados

---

##  Arquitectura

### Arquitectura General

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│                 │     │                 │     │                 │
│   Controller    │────▶│    Service      │────▶│   Repository    │
│                 │     │                 │     │                 │
└─────────────────┘     └────────┬────────┘     └─────────────────┘
                                 │                       │
                                 ▼                       ▼
                        ┌─────────────────┐     ┌─────────────────┐
                        │  QrCacheService │     │                 │
                        │   (Redis)       │     │     MySQL       │
                        │                 │     │                 │
                        └─────────────────┘     └─────────────────┘
```

### Arquitectura de Servicios de Caché

```
                    ┌──────────────────────────────┐
                    │  AbstractRedisCacheService   │
                    │  (Clase Base Abstracta)      │
                    │  - RedisTemplate             │
                    │  - buildKey()                │
                    │  - setWithExpiry()           │
                    │  - delete()                  │
                    └──────────────┬───────────────┘
                                   │
                 ┌─────────────────┼─────────────────┐
                 │                 │                 │
                 ▼                 ▼                 ▼
    ┌────────────────────┐  ┌──────────────┐  ┌────────────────┐
    │ QrRedisCache       │  │ CategoryFaq  │  │ SupportFaq     │
    │ ServiceImpl        │  │ CacheService │  │ CacheService   │
    │ - IQrCacheService  │  │ - ICacheSvc  │  │ - ICacheSvc    │
    └────────────────────┘  └──────────────┘  └────────────────┘
```

---

##  Tecnologías

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17+ | Lenguaje principal |
| Spring Boot | 3.x | Framework backend |
| MySQL | 8.x | Base de datos principal |
| Redis | 7.x | Caché de alta velocidad |
| JPA/Hibernate | - | ORM |
| Lombok | - | Reducción de boilerplate |

---

##  Configuración

### application.properties 


# Redis
spring.data.redis.host=${REDIS_HOST}
spring.data.redis.port=${REDIS_PORT}
spring.data.redis.password=${REDIS_PASSWORD}
spring.data.redis.timeout=60000

# MySQL
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}


### Docker Compose (Opcional)

```yaml
version: '3.8'
services:
  redis-metro:
    image: redis:alpine
    container_name: redis-metro
    ports:
      - "${REDIS_PORT}:6379"
    command:  redis-server --requirepass "${REDIS_PASSWORD}" --notify-keyspace-events Ex
    restart: always
    networks:
      - metro-network


networks:
  metro-network:
    driver: bridge


volumes:
  redis-data:
    driver: local
```

---

##  Flujo de Funcionamiento

### Generación de QR

```
1. Usuario solicita QR
         │
         ▼
2. ¿Existe en Redis Cache?
    │           │
   SÍ          NO
    │           │
    ▼           ▼
3. Retornar   4. ¿Existe QR activo en BD?
   del cache      │           │
                 SÍ          NO
                  │           │
                  ▼           ▼
             5. Cachear    6. ¿Existe QR expirado?
                y retornar     │           │
                              SÍ          NO
                               │           │
                               ▼           ▼
                          7. Mover a    8. Crear
                             histórico     nuevo QR
                             + crear nuevo
```

### Código del Flujo Principal

```java
@Override
@Transactional
public QrResponseDTO generarQrAcceso(String email) {
    Usuario usuario = usuarioRepository.findByCorreo(email)
            .orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_NOT_FOUND));

    Long usuarioId = usuario.getId();
    LocalDateTime ahora = LocalDateTime.now();

    // 1. Intentar obtener desde caché
    QrResponseDTO cacheado = obtenerDesdeCache(usuarioId, ahora);
    if (cacheado != null) {
        return cacheado;
    }

    // 2. Buscar QR activo en BD
    Optional<Qr> activoOpt = qrRepository.findQrActivoByUsuario(usuarioId, ahora);
    if (activoOpt.isPresent()) {
        return cachearYRetornar(activoOpt.get());
    }

    // 3. Procesar QR no consumido (puede estar expirado)
    Optional<Qr> ultimoOpt = qrRepository.findLatestNotConsumedQrForUserForUpdate(usuarioId);
    if (ultimoOpt.isPresent()) {
        return procesarQrExistente(ultimoOpt.get(), usuario, ahora);
    }

    // 4. No existe ningún QR → crear nuevo
    return crearYCachearNuevoQr(usuario);
}
```

---

## 📁 Estructura de Archivos

```
src/main/java/com/sena/BogotaMetroApp/
├── externalservices/
│   ├── cache/
│   │   ├── AbstractRedisCacheService.java      # Clase base abstracta para servicios de caché
│   │   ├── IQrCacheService.java                # Interface del servicio de caché de QR
│   │   ├── QrRedisQrCacheServiceImpl.java      # Implementación del servicio de caché de QR
│   │   ├── ICategoryFaqCacheService.java       # Interface del servicio de caché de categorías
│   │   ├── CategoryFaqRedisCacheServiceImpl.java # Implementación del servicio de caché de categorías
│   │   ├── ISupportFaqCacheService.java        # Interface del servicio de caché de FAQs
│   │   └── FaqRedisCacheServiceImpl.java       # Implementación del servicio de caché de FAQs
│   ├── ChatRedisService.java                   # Servicio Redis para chat
│   └── ChatExpirationListener.java             # Listener para expiración de chat
├── jobs/
│   └── QrCleanupJob.java                        # Job programado para limpieza de QR expirados
├── mapper/qr/
│   └── QrMapper.java                            # Mapeo entre entidades y DTOs
├── persistence/
│   ├── models/qr/
│   │   ├── Qr.java                              # Entidad QR principal
│   │   └── QrNoUsado.java                       # Entidad QR histórico
│   └── repository/qr/
│       ├── QrRepository.java                    # Repositorio QR con queries personalizadas
│       └── QrNoUsadoRepository.java             # Repositorio histórico
├── presentation/dto/
│   ├── QrCacheDTO.java                          # DTO para Redis
│   └── qr/
│       └── QrResponseDTO.java                   # DTO de respuesta
├── services/qr/
│   ├── IQrService.java                          # Interface del servicio
│   ├── QrServiceImpl.java                       # Implementación del servicio principal
│   ├── IQrNoUsadoService.java                   # Interface histórico
│   └── QrNoUsadoServiceImpl.java                # Implementación histórico
└── utils/
    ├── enums/
    │   └── TipoQr.java                          # Enum tipos de QR
    └── validators/
        └── QrValidator.java                     # Validaciones de QR
```

### Jerarquía de Clases de Caché

```
AbstractRedisCacheService (Abstracta)
│
├── Métodos Comunes:
│   ├── buildKey(String... parts)
│   ├── setWithExpiry(String key, Object value, Duration duration)
│   ├── get(String key, Class<T> type)
│   ├── delete(String key)
│   └── exists(String key)
│
└── Implementaciones Concretas:
    ├── QrRedisQrCacheServiceImpl
    │   └── Gestiona: usuario:{id}:qr, qr:{codigo}
    ├── CategoryFaqRedisCacheServiceImpl
    │   └── Gestiona: categoryfaqs:all
    └── FaqRedisCacheServiceImpl
        └── Gestiona: supportfaqs:all, supportfaqs:category:{id}
```

---

## 🏗 Arquitectura de Caché Redis

### AbstractRedisCacheService - Clase Base

Clase abstracta que proporciona funcionalidad común de caché para todos los servicios Redis:

```java
@Slf4j
public abstract class AbstractRedisCacheService {
    protected final RedisTemplate<String, Object> redisTemplate;
    
    // Construir claves de forma consistente
    protected String buildKey(String... parts) {
        return String.join(":", parts);
    }
    
    // Guardar con expiración
    protected <T> void setWithExpiry(String key, T value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }
    
    // Obtener valor tipado
    protected <T> Optional<T> get(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(value != null ? type.cast(value) : null);
    }
    
    // Eliminar clave
    protected void delete(String key) {
        redisTemplate.delete(key);
    }
}
```

### Implementación Específica: QrRedisQrCacheServiceImpl

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
        
        // Guardar con script Lua para versionamiento optimista
        saveQrWithVersioning(userKey, qrCacheDTO, duration);
        setWithExpiry(qrKey, qrCacheDTO, duration);
    }
    
    @Override
    public Optional<QrCacheDTO> getQrByUsuarioId(Long usuarioId) {
        String key = buildKey(USER_QR_PREFIX, usuarioId.toString(), "qr");
        return get(key, QrCacheDTO.class);
    }
}
```

### Ventajas de esta Arquitectura

1. **Reutilización de Código**: Métodos comunes en clase base
2. **Consistencia**: Todas las implementaciones siguen el mismo patrón
3. **Mantenibilidad**: Cambios en la lógica base se propagan automáticamente
4. **Extensibilidad**: Fácil agregar nuevos servicios de caché
5. **Testing**: Facilita las pruebas unitarias y de integración

---

## 🌐 API Endpoints

### Generar QR de Acceso

```http
POST /api/qr/acceso
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
    "id": 1,
    "tipo": "ACCESO",
    "contenidoQr": "ACCESO:2:2:a3c87577:1765432137903",
    "fechaGeneracion": "2025-12-11T02:23:51.290172",
    "fechaExpiracion": "2025-12-11T02:25:51.290172"
}
```

### Validar QR (Torniquete)

```http
POST /api/qr/validar
Content-Type: application/json

{
    "contenidoQr": "ACCESO:2:2:a3c87577:1765432137903"
}
```

---

## 💾 Caché Redis

### Estructura de Keys

| Key Pattern | Descripción | TTL | Ejemplo |
|-------------|-------------|-----|---------|
| `usuario:{id}:qr` | QR activo por usuario | Tiempo restante hasta expiración | `usuario:2:qr` |
| `qr:{codigo}` | QR por código | Tiempo restante hasta expiración | `qr:ACCESO:2:2:56de5dfb:1765437831294` |

### Campos Almacenados

```
HGETALL usuario:2:qr
1) "id"
2) "8"
3) "usuarioId"
4) "2"
5) "codigo"
6) "ACCESO:2:2:56de5dfb:1765437831294"
7) "tipo"
8) "ACCESO"
9) "fechaGeneracion"
10) "2025-12-11T02:23:51.290172"
11) "fechaExpiracion"
12) "2025-12-11T02:25:51.290172"
13) "consumido"
14) "false"
15) "version"
16) "1"
```

### Versionamiento Optimista (Lua Script)

```lua
local existing = redis.call('HGET', KEYS[1], 'version')
if (not existing) or (tonumber(ARGV[1]) > tonumber(existing)) then
    redis.call('HMSET', KEYS[1], ...)
    redis.call('EXPIRE', KEYS[1], ARGV[2])
    return 1
end
return 0
```

### Listener de Expiración

El sistema utiliza notificaciones de Redis para escuchar eventos de expiración:

```java
@Component
public class ChatExpirationListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("Clave expirada en Redis: {}", expiredKey);
        // Lógica adicional si es necesaria
    }
}
```

**Configuración requerida en Redis:**
```bash
redis-server --notify-keyspace-events Ex
```

---

## 🗂 Manejo de QR Expirados

### Job de Limpieza Automática

El sistema incluye un job programado que limpia automáticamente los QR expirados:

```java
@Component
@Slf4j
@RequiredArgsConstructor
public class QrCleanupJob {
    private final QrRepository qrRepository;
    private final IQrCacheService qrCacheService;
    private final IQrNoUsadoService qrNoUsadoService;
    
    @Scheduled(cron = "0 */5 * * * ?") // Cada 5 minutos
    @Transactional
    public void cleanupExpiredQrs() {
        LocalDateTime now = LocalDateTime.now();
        List<Qr> expirados = qrRepository.findAllByFechaExpiracionBeforeAndConsumido(now, false);
        
        if (!expirados.isEmpty()) {
            log.info("Limpiando {} QRs expirados", expirados.size());
            
            for (Qr qr : expirados) {
                // 1. Mover a histórico
                qrNoUsadoService.moverQrNoUsadoAndExpirado(qr);
                
                // 2. Eliminar de cache
                qrCacheService.deleteQrFromCache(qr.getUsuario().getId(), qr.getContenidoQr());
                
                // 3. Eliminar de BD principal
                qrRepository.delete(qr);
            }
            
            log.info("Limpieza completada: {} QRs movidos a histórico", expirados.size());
        }
    }
}
```

**Configuración del Job:**
- **Frecuencia**: Cada 5 minutos
- **Expresión Cron**: `0 */5 * * * ?`
- **Transaccional**: Sí

### Tabla de Histórico

```sql
CREATE TABLE qrs_no_usados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contenido_qr VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    fecha_generacion DATETIME(6) NOT NULL,
    fecha_expiracion DATETIME(6) NOT NULL,
    fecha_movimiento DATETIME(6) NOT NULL,
    usuario_id_usuario BIGINT NOT NULL,
    FOREIGN KEY (usuario_id_usuario) REFERENCES usuario(id_usuario)
);
```

### Flujo de Movimiento

```java
@Override
@Transactional
public void moverQrNoUsadoAndExpirado(Qr qr) {
    QrNoUsado qrNoUsado = QrNoUsado.builder()
            .contenidoQr(qr.getContenidoQr())
            .tipo(qr.getTipo())
            .fechaGeneracion(qr.getFechaGeneracion())
            .fechaExpiracion(qr.getFechaExpiracion())
            .fechaMovimiento(LocalDateTime.now())
            .usuario(qr.getUsuario())
            .build();
    
    qrNoUsadoRepository.save(qrNoUsado);
    log.info("QR movido a histórico: {}", qr.getContenidoQr());
}
```

### Proceso Completo de Limpieza

```
┌─────────────────────────────────────────┐
│  Job ejecuta cada 5 minutos             │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  Buscar QRs expirados y no consumidos   │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  Para cada QR expirado:                 │
│  1. Mover a qrs_no_usados (histórico)   │
│  2. Eliminar de Redis Cache             │
│  3. Eliminar de tabla qrs (principal)   │
└─────────────────────────────────────────┘
```

---

## 🔧 Solución de Problemas

### Problema: QR expirado sigue siendo retornado como activo

**Causa:** Desincronización de zona horaria entre Java y MySQL.

**Solución:** Usar parámetro en lugar de `CURRENT_TIMESTAMP`:

```java
//  Incorrecto
@Query("SELECT q FROM Qr q WHERE ... AND q.fechaExpiracion > CURRENT_TIMESTAMP")
Optional<Qr> findQrActivoByUsuario(@Param("usuarioId") Long usuarioId);

//  Correcto
@Query("SELECT q FROM Qr q WHERE ... AND q.fechaExpiracion > :ahora")
Optional<Qr> findQrActivoByUsuario(@Param("usuarioId") Long usuarioId, @Param("ahora") LocalDateTime ahora);
```

### Problema: Campos null al retornar desde caché

**Causa:** Campos no incluidos en el script de Redis.

**Solución:** Asegurar que todos los campos estén en el script Lua y en `mapFromHash()`.

### Problema: Cache no se encuentra aunque fue guardado

**Causa:** TTL expiró o Redis no está corriendo.

**Verificación:**
```bash
redis-cli ping
# Debe responder: PONG

redis-cli HGETALL usuario:2:qr
# Debe mostrar los campos del QR
```

### Limpiar Cache Manualmente

```bash
redis-cli FLUSHALL
```

---

##  Métricas y Logs

### Logs Importantes

```
# QR creado exitosamente
INFO: Nuevo QR creado: id=6, fechaExp=2025-12-11T02:14:06

# Cache guardado
INFO: Resultado cache userKey [usuario:2:qr]: 1
INFO: TTL aplicado a userKey: 119 segundos

# Retornando desde cache
INFO: Cache encontrado - fechaExp: ..., ahora: ..., consumido: false
INFO: Retornando QR desde cache (válido)

# QR expirado movido a histórico
INFO: QR expirado - moviendo a histórico y creando nuevo
```

---

##  Mejoras Futuras

- [ ] Implementar rate limiting por usuario
- [ ] Agregar métricas con Micrometer/Prometheus
- [ ] Implementar QR para diferentes tipos (recarga, transferencia)
- [ ] Agregar notificaciones push cuando el QR está por expirar
- [ ] Implementar QR offline con firma digital
- [ ] Dashboard de monitoreo de QR activos/expirados
- [ ] Integración con sistema de auditoría

---

##  Documentación Relacionada

- [Sistema de Categorías FAQ](README-CategoryFaq-System.md)
- [Sistema de Support FAQ](README-SupportFaq-System.md)
- [Arquitectura de Caché Redis](README-Redis-Cache-Architecture.md)

---

##  Contribuidores

- **Desarrollo inicial**: Sebastian David Escorcia Montes
- **Colaboradores**: Jeniffer Saumeth

---

##  Licencia

Este proyecto es parte del sistema BogotaMetroApp - SENA 2025 
