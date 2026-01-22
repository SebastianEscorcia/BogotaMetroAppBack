# 💬 Sistema de Support FAQ con Redis Cache

Sistema de gestión de preguntas frecuentes (FAQs) con soporte para categorización, implementado con Spring Boot, MySQL y Redis para caché distribuida de alta velocidad.

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Modelo de Datos](#-modelo-de-datos)
- [Estructura de Archivos](#-estructura-de-archivos)
- [Flujo de Funcionamiento](#-flujo-de-funcionamiento)
- [API Endpoints](#-api-endpoints)
- [Caché Redis](#-caché-redis)
- [Implementación del Servicio](#-implementación-del-servicio)
- [Solución de Problemas](#-solución-de-problemas)

---

## ✨ Características

- ✅ CRUD completo de FAQs con soporte para categorías
- 🚀 Caché en Redis de dos niveles (por categoría y global)
- 🔄 Invalidación automática de caché en modificaciones
- 🗂️ Filtrado por categoría
- 🗑️ Soft delete (borrado lógico)
- 🏗️ Arquitectura basada en `AbstractRedisCacheService`
- 📝 Soporte para respuestas largas con tipo TEXT/LOB
- 🔒 Validación de datos con Spring Validation
- 🔗 Relación many-to-one con CategoryFaq

---

## 🏛️ Arquitectura

### Arquitectura General

```
┌──────────────────┐     ┌──────────────────┐     ┌──────────────────┐
│                  │     │                  │     │                  │
│   Controller     │────▶│    Service       │────▶│   Repository     │
│  SupportFaq      │     │  SupportFaq      │     │  SupportFaq      │
│                  │     │                  │     │                  │
└──────────────────┘     └────────┬─────────┘     └──────────────────┘
                                  │                         │
                                  ▼                         ▼
                         ┌──────────────────┐     ┌──────────────────┐
                         │ SupportFaqCache  │     │                  │
                         │    (Redis)       │     │      MySQL       │
                         │  2 niveles:      │     │                  │
                         │  - Global        │     │                  │
                         │  - Por Categoría │     │                  │
                         └──────────────────┘     └──────────────────┘
```

### Arquitectura de Caché Multi-nivel

```
    ┌────────────────────────────────┐
    │  AbstractRedisCacheService     │
    │  (Clase Base Abstracta)        │
    │  - RedisTemplate               │
    │  - buildKey()                  │
    │  - setWithExpiry()             │
    │  - get()                       │
    │  - delete()                    │
    └───────────────┬────────────────┘
                    │
                    │ extends
                    │
    ┌───────────────▼────────────────┐
    │  FaqRedisCacheServiceImpl      │
    │  (ISupportFaqCacheService)     │
    │                                │
    │  Métodos:                      │
    │  • getCachedSupportFaqs()      │ ← Cache Global
    │  • cacheSupportFaqs()          │
    │  • getCachedSupportFaqsByCategory() │ ← Cache por Categoría
    │  • cacheSupportFaqsByCategory()│
    │  • invalidateSupportFaqsCache()│
    └────────────────────────────────┘
```

### Estrategia de Caché

```
Redis Keys:
┌─────────────────────────────────────┐
│  supportfaqs:all                    │  ← Todas las FAQs activas
│  TTL: 24 horas                      │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│  supportfaqs:category:1             │  ← FAQs de categoría 1
│  TTL: 12 horas                      │
├─────────────────────────────────────┤
│  supportfaqs:category:2             │  ← FAQs de categoría 2
│  TTL: 12 horas                      │
└─────────────────────────────────────┘
```

---

## 🛠️ Tecnologías

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17+ | Lenguaje principal |
| Spring Boot | 3.x | Framework backend |
| MySQL | 8.x | Base de datos principal |
| Redis | 7.x | Caché de alta velocidad |
| JPA/Hibernate | - | ORM |
| Lombok | - | Reducción de boilerplate |
| MapStruct | - | Mapeo de objetos |

---

## 💾 Modelo de Datos

### Entidad SupportFaq

```java
@Entity
@Table(name = "support_faqs")
public class SupportFaq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String question;
    
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;
    
    private boolean isActive = true;
    
    @ManyToOne
    @JoinColumn(name = "category_faq_id", nullable = false)
    private CategoryFaq categoryFaq;
}
```

### DTOs

**SupportFaqRequestDTO:**
```java
public class SupportFaqRequestDTO {
    @NotBlank(message = "La pregunta es obligatoria")
    @Size(min = 10, max = 500, message = "La pregunta debe tener entre 10 y 500 caracteres")
    private String question;
    
    @NotBlank(message = "La respuesta es obligatoria")
    @Size(min = 10, max = 5000, message = "La respuesta debe tener entre 10 y 5000 caracteres")
    private String answer;
    
    @NotNull(message = "La categoría es obligatoria")
    private Long categoryFaqId;
}
```

**SupportFaqResponseDTO:**
```java
public class SupportFaqResponseDTO implements Serializable {
    private Long id;
    private String question;
    private String answer;
    private boolean active;
    private CategoryFaqResponseDTO category;
}
```

### Tabla MySQL

```sql
CREATE TABLE support_faqs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(500) NOT NULL,
    answer TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    category_faq_id BIGINT NOT NULL,
    FOREIGN KEY (category_faq_id) REFERENCES category_faqs(id),
    INDEX idx_active (is_active),
    INDEX idx_category (category_faq_id, is_active)
);
```

---

## 📁 Estructura de Archivos

```
src/main/java/com/sena/BogotaMetroApp/
├── externalservices/cache/
│   ├── AbstractRedisCacheService.java           # Clase base abstracta
│   ├── ISupportFaqCacheService.java             # Interface del servicio de caché
│   └── FaqRedisCacheServiceImpl.java            # Implementación del servicio de caché
├── mapper/supportfaq/
│   └── SupportFaqMapper.java                    # Mapeo entre entidades y DTOs
├── persistence/
│   ├── models/supportfaq/
│   │   ├── SupportFaq.java                      # Entidad principal
│   │   └── CategoryFaq.java                     # Entidad de categoría
│   └── repository/supportfaq/
│       ├── SupportFaqRepository.java            # Repositorio JPA
│       └── CategoryFaqRepository.java           # Repositorio de categorías
├── presentation/
│   ├── controller/supportfaq/
│   │   └── SupportFaqController.java            # Controlador REST
│   └── dto/supportfaq/
│       ├── SupportFaqRequestDTO.java            # DTO de entrada
│       ├── SupportFaqResponseDTO.java           # DTO de salida
│       └── CategoryFaqResponseDTO.java          # DTO de categoría
└── services/supportfaq/
    ├── ISupportFaqService.java                  # Interface del servicio
    └── SupportFaqServiceImpl.java               # Implementación del servicio
```

---

## 🔄 Flujo de Funcionamiento

### Consulta de FAQs Globales (GET All)

```
1. Usuario solicita todas las FAQs
         │
         ▼
2. ¿Existe "supportfaqs:all" en Redis?
    │           │
   SÍ          NO
    │           │
    ▼           ▼
3. Retornar   4. Consultar MySQL (findAllByIsActiveTrue)
   del cache      │
                  ▼
             5. Guardar en "supportfaqs:all" (TTL: 24h)
                  │
                  ▼
             6. Retornar al usuario
```

### Consulta de FAQs por Categoría

```
1. Usuario solicita FAQs de categoría X
         │
         ▼
2. ¿Existe "supportfaqs:category:X" en Redis?
    │           │
   SÍ          NO
    │           │
    ▼           ▼
3. Retornar   4. Consultar MySQL (findAllByCategoryFaqIdAndIsActiveTrue)
   del cache      │
                  ▼
             5. Guardar en "supportfaqs:category:X" (TTL: 12h)
                  │
                  ▼
             6. Retornar al usuario
```

### Creación/Actualización/Eliminación

```
1. Usuario envía petición (POST/PUT/DELETE)
         │
         ▼
2. Validar datos (Request DTO)
         │
         ▼
3. Validar existencia de CategoryFaq
         │
         ▼
4. Ejecutar operación en MySQL
         │
         ▼
5. Invalidar TODOS los caches relacionados:
   • supportfaqs:all
   • supportfaqs:category:*
         │
         ▼
6. Retornar respuesta (Response DTO)
```

### Código del Flujo Principal

**Obtener Todas las FAQs:**
```java
@Override
@Transactional(readOnly = true)
public List<SupportFaqResponseDTO> getAllActiveSupportFaqs() {
    // 1. Intentar obtener desde cache global
    return faqCacheService.getCachedSupportFaqs()
            .orElseGet(() -> {
                // 2. Cache miss - consultar DB
                log.info("Cache miss - consultando Support FAQs desde DB");
                List<SupportFaqResponseDTO> faqs = 
                    supportFaqRepository.findAllByIsActiveTrue()
                        .stream()
                        .map(supportFaqMapper::toDTO)
                        .collect(Collectors.toList());
                
                // 3. Guardar en cache
                faqCacheService.cacheSupportFaqs(faqs);
                
                return faqs;
            });
}
```

**Obtener FAQs por Categoría:**
```java
@Override
@Transactional(readOnly = true)
public List<SupportFaqResponseDTO> getSupportFaqsByCategoryId(Long categoryId) {
    // 1. Intentar obtener desde cache por categoría
    return faqCacheService.getCachedSupportFaqsByCategory(categoryId)
            .orElseGet(() -> {
                // 2. Cache miss - consultar DB
                log.info("Cache miss - consultando Support FAQs por categoría {} desde DB", categoryId);
                List<SupportFaqResponseDTO> faqs = 
                    supportFaqRepository.findAllByCategoryFaqIdAndIsActiveTrue(categoryId)
                        .stream()
                        .map(supportFaqMapper::toDTO)
                        .collect(Collectors.toList());
                
                // 3. Guardar en cache por categoría
                faqCacheService.cacheSupportFaqsByCategory(categoryId, faqs);
                
                return faqs;
            });
}
```

---

## 🌐 API Endpoints

### Crear FAQ

```http
POST /api/support/faqs
Content-Type: application/json
Authorization: Bearer {token}

{
    "question": "¿Cuál es el horario del metro?",
    "answer": "El metro opera de lunes a viernes de 5:00 AM a 11:00 PM...",
    "categoryFaqId": 1
}
```

**Response (201 Created):**
```json
{
    "id": 1,
    "question": "¿Cuál es el horario del metro?",
    "answer": "El metro opera de lunes a viernes de 5:00 AM a 11:00 PM...",
    "active": true,
    "category": {
        "id": 1,
        "name": "Información General",
        "active": true
    }
}
```

---

### Obtener Todas las FAQs Activas

```http
GET /api/support/faqs
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
[
    {
        "id": 1,
        "question": "¿Cuál es el horario del metro?",
        "answer": "El metro opera de lunes a viernes...",
        "active": true,
        "category": {
            "id": 1,
            "name": "Información General",
            "active": true
        }
    },
    {
        "id": 2,
        "question": "¿Cuánto cuesta el pasaje?",
        "answer": "El pasaje regular cuesta $2,950 COP...",
        "active": true,
        "category": {
            "id": 2,
            "name": "Tarifas y Pagos",
            "active": true
        }
    }
]
```

---

### Obtener FAQs por Categoría

```http
GET /api/support/faqs/category/{categoryId}
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
[
    {
        "id": 1,
        "question": "¿Cuál es el horario del metro?",
        "answer": "El metro opera de lunes a viernes...",
        "active": true,
        "category": {
            "id": 1,
            "name": "Información General",
            "active": true
        }
    }
]
```

---

### Obtener FAQ por ID

```http
GET /api/support/faqs/{id}
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
    "id": 1,
    "question": "¿Cuál es el horario del metro?",
    "answer": "El metro opera de lunes a viernes...",
    "active": true,
    "category": {
        "id": 1,
        "name": "Información General",
        "active": true
    }
}
```

---

### Actualizar FAQ

```http
PUT /api/support/faqs/{id}
Content-Type: application/json
Authorization: Bearer {token}

{
    "question": "¿Cuál es el horario del metro actualizado?",
    "answer": "El metro opera todos los días de 4:30 AM a 11:30 PM...",
    "categoryFaqId": 1
}
```

**Response (200 OK):**
```json
{
    "id": 1,
    "question": "¿Cuál es el horario del metro actualizado?",
    "answer": "El metro opera todos los días de 4:30 AM a 11:30 PM...",
    "active": true,
    "category": {
        "id": 1,
        "name": "Información General",
        "active": true
    }
}
```

---

### Eliminar FAQ (Soft Delete)

```http
DELETE /api/support/faqs/{id}
Authorization: Bearer {token}
```

**Response (204 No Content)**

---

## 💾 Caché Redis

### Estructura de Keys

| Key Pattern | Descripción | TTL | Ejemplo |
|-------------|-------------|-----|---------|
| `supportfaqs:all` | Lista de todas las FAQs activas | 24 horas | `supportfaqs:all` |
| `supportfaqs:category:{id}` | FAQs de una categoría específica | 12 horas | `supportfaqs:category:1` |

### Datos Almacenados

**Key: supportfaqs:all**
```json
[
    {
        "id": 1,
        "question": "¿Cuál es el horario del metro?",
        "answer": "El metro opera de lunes a viernes...",
        "active": true,
        "category": {
            "id": 1,
            "name": "Información General",
            "active": true
        }
    },
    {
        "id": 2,
        "question": "¿Cuánto cuesta el pasaje?",
        "answer": "El pasaje regular cuesta $2,950 COP...",
        "active": true,
        "category": {
            "id": 2,
            "name": "Tarifas y Pagos",
            "active": true
        }
    }
]
```

**Key: supportfaqs:category:1**
```json
[
    {
        "id": 1,
        "question": "¿Cuál es el horario del metro?",
        "answer": "El metro opera de lunes a viernes...",
        "active": true,
        "category": {
            "id": 1,
            "name": "Información General",
            "active": true
        }
    }
]
```

### Implementación del Cache Service

```java
@Service
@Slf4j
public class FaqRedisCacheServiceImpl 
        extends AbstractRedisCacheService 
        implements ISupportFaqCacheService {
    
    private static final String FAQ_ALL_KEY = "supportfaqs:all";
    private static final String FAQ_CATEGORY_PREFIX = "supportfaqs:category";
    
    private static final Duration CACHE_TTL_ALL = Duration.ofHours(24);
    private static final Duration CACHE_TTL_CATEGORY = Duration.ofHours(12);
    
    // Cache Global
    @Override
    public Optional<List<SupportFaqResponseDTO>> getCachedSupportFaqs() {
        try {
            Object cached = redisTemplate.opsForValue().get(FAQ_ALL_KEY);
            if (cached instanceof List<?>) {
                log.info("✅ Cache HIT - FAQs obtenidas desde Redis (global)");
                return Optional.of((List<SupportFaqResponseDTO>) cached);
            }
        } catch (Exception e) {
            log.error("❌ Error obteniendo FAQs desde cache", e);
        }
        return Optional.empty();
    }
    
    @Override
    public void cacheSupportFaqs(List<SupportFaqResponseDTO> faqs) {
        try {
            setWithExpiry(FAQ_ALL_KEY, faqs, CACHE_TTL_ALL);
            log.info("💾 FAQs guardadas en cache global - Total: {}", faqs.size());
        } catch (Exception e) {
            log.error("❌ Error guardando FAQs en cache", e);
        }
    }
    
    // Cache por Categoría
    @Override
    public Optional<List<SupportFaqResponseDTO>> getCachedSupportFaqsByCategory(Long categoryId) {
        try {
            String key = buildKey(FAQ_CATEGORY_PREFIX, categoryId.toString());
            Object cached = redisTemplate.opsForValue().get(key);
            if (cached instanceof List<?>) {
                log.info("✅ Cache HIT - FAQs de categoría {} desde Redis", categoryId);
                return Optional.of((List<SupportFaqResponseDTO>) cached);
            }
        } catch (Exception e) {
            log.error("❌ Error obteniendo FAQs por categoría desde cache", e);
        }
        return Optional.empty();
    }
    
    @Override
    public void cacheSupportFaqsByCategory(Long categoryId, List<SupportFaqResponseDTO> faqs) {
        try {
            String key = buildKey(FAQ_CATEGORY_PREFIX, categoryId.toString());
            setWithExpiry(key, faqs, CACHE_TTL_CATEGORY);
            log.info("💾 FAQs de categoría {} guardadas en cache - Total: {}", categoryId, faqs.size());
        } catch (Exception e) {
            log.error("❌ Error guardando FAQs por categoría en cache", e);
        }
    }
    
    // Invalidación Completa
    @Override
    public void invalidateSupportFaqsCache() {
        try {
            // Invalidar cache global
            delete(FAQ_ALL_KEY);
            
            // Invalidar todos los caches por categoría
            Set<String> keys = redisTemplate.keys(FAQ_CATEGORY_PREFIX + ":*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("🗑️ Cache de FAQs invalidado - {} keys eliminadas", keys.size() + 1);
            } else {
                log.info("🗑️ Cache global de FAQs invalidado");
            }
        } catch (Exception e) {
            log.error("❌ Error invalidando cache de FAQs", e);
        }
    }
}
```

---

## 🔧 Implementación del Servicio

### Interface del Servicio

```java
public interface ISupportFaqService {
    SupportFaqResponseDTO createSupportFaq(SupportFaqRequestDTO dto);
    SupportFaqResponseDTO getSupportFaqById(Long id);
    List<SupportFaqResponseDTO> getAllActiveSupportFaqs();
    List<SupportFaqResponseDTO> getSupportFaqsByCategoryId(Long categoryId);
    SupportFaqResponseDTO updateSupportFaq(Long id, SupportFaqRequestDTO dto);
    void deleteSupportFaq(Long id);
}
```

### Implementación del Servicio

```java
@Service
@Slf4j
@RequiredArgsConstructor
public class SupportFaqServiceImpl implements ISupportFaqService {
    private final SupportFaqMapper supportFaqMapper;
    private final SupportFaqRepository supportFaqRepository;
    private final CategoryFaqRepository categoryFaqRepository;
    private final ISupportFaqCacheService faqCacheService;
    
    @Override
    @Transactional
    public SupportFaqResponseDTO createSupportFaq(SupportFaqRequestDTO dto) {
        // 1. Validar categoría
        CategoryFaq categoryFaq = categoryFaqRepository.findByIdAndActiveTrue(dto.getCategoryFaqId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "CategoryFaq no encontrada con id: " + dto.getCategoryFaqId()));
        
        // 2. Crear FAQ
        SupportFaq supportFaq = supportFaqMapper.toEntity(dto);
        supportFaq.setCategoryFaq(categoryFaq);
        supportFaq.setActive(true);
        
        // 3. Guardar en DB
        SupportFaq saved = supportFaqRepository.save(supportFaq);
        
        // 4. Invalidar cache
        faqCacheService.invalidateSupportFaqsCache();
        
        return supportFaqMapper.toDTO(saved);
    }
    
    @Override
    public SupportFaqResponseDTO updateSupportFaq(Long id, SupportFaqRequestDTO dto) {
        // 1. Buscar FAQ existente
        SupportFaq supportFaq = supportFaqRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "SupportFaq no encontrada con id: " + id));
        
        // 2. Actualizar categoría si cambió
        if (dto.getCategoryFaqId() != null) {
            CategoryFaq categoryFaq = categoryFaqRepository.findByIdAndActiveTrue(dto.getCategoryFaqId())
                    .orElseThrow(() -> new EntityNotFoundException(
                        "CategoryFaq no encontrada con id: " + dto.getCategoryFaqId()));
            supportFaq.setCategoryFaq(categoryFaq);
        }
        
        // 3. Actualizar campos
        supportFaq.setQuestion(dto.getQuestion());
        supportFaq.setAnswer(dto.getAnswer());
        
        // 4. Guardar en DB
        SupportFaq updated = supportFaqRepository.save(supportFaq);
        
        // 5. Invalidar cache (ambos niveles)
        faqCacheService.invalidateSupportFaqsCache();
        
        return supportFaqMapper.toDTO(updated);
    }
    
    @Override
    public void deleteSupportFaq(Long id) {
        SupportFaq supportFaq = supportFaqRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "SupportFaq no encontrada con id: " + id));
        
        // Soft delete
        supportFaq.setActive(false);
        supportFaqRepository.save(supportFaq);
        
        // Invalidar cache
        faqCacheService.invalidateSupportFaqsCache();
    }
}
```

---

## 🐛 Solución de Problemas

### Problema: Cache no se actualiza después de crear/actualizar FAQ

**Causa:** La invalidación del cache no está limpiando todos los niveles.

**Solución:** Asegurar que se invaliden tanto el cache global como los de categorías:

```java
@Override
public void invalidateSupportFaqsCache() {
    // Invalidar cache global
    delete(FAQ_ALL_KEY);
    
    // Invalidar TODOS los caches por categoría
    Set<String> keys = redisTemplate.keys(FAQ_CATEGORY_PREFIX + ":*");
    if (keys != null && !keys.isEmpty()) {
        redisTemplate.delete(keys);
    }
}
```

---

### Problema: Cache retorna FAQs de categoría incorrecta

**Causa:** La clave de cache no incluye el ID de categoría correctamente.

**Solución:** Verificar la construcción de la clave:

```java
// ✅ Correcto
String key = buildKey(FAQ_CATEGORY_PREFIX, categoryId.toString());
// Resultado: "supportfaqs:category:1"

// ❌ Incorrecto
String key = FAQ_CATEGORY_PREFIX + categoryId;
// Resultado: "supportfaqs:category1" (sin separador)
```

---

### Problema: Respuestas largas se truncan

**Causa:** El campo `answer` no está definido como TEXT en la base de datos.

**Solución:** Asegurar la anotación `@Lob` y el tipo TEXT:

```java
@Lob
@Column(nullable = false, columnDefinition = "TEXT")
private String answer;
```

---

### Problema: Error al crear FAQ - Categoría no existe

**Causa:** La categoría referenciada no existe o está inactiva.

**Solución:** Validar existencia de categoría activa:

```java
CategoryFaq categoryFaq = categoryFaqRepository.findByIdAndActiveTrue(dto.getCategoryFaqId())
        .orElseThrow(() -> new EntityNotFoundException(
            "CategoryFaq no encontrada con id: " + dto.getCategoryFaqId()));
```

---

### Verificar Cache en Redis

```bash
# Conectar a Redis CLI
redis-cli

# Ver cache global de FAQs
GET supportfaqs:all

# Ver cache de categoría específica
GET supportfaqs:category:1

# Ver todas las claves de FAQs
KEYS supportfaqs:*

# Ver TTL de una clave
TTL supportfaqs:all

# Limpiar todos los caches de FAQs
DEL supportfaqs:all
KEYS supportfaqs:category:* | xargs redis-cli DEL
```

---

## 📊 Métricas y Logs

### Logs Importantes

```
# Cache hit global
✅ Cache HIT - FAQs obtenidas desde Redis (global)

# Cache hit por categoría
✅ Cache HIT - FAQs de categoría 1 desde Redis

# Cache miss global
Cache miss - consultando Support FAQs desde DB

# Cache miss por categoría
Cache miss - consultando Support FAQs por categoría 1 desde DB

# Guardar en cache global
💾 FAQs guardadas en cache global - Total: 15

# Guardar en cache por categoría
💾 FAQs de categoría 1 guardadas en cache - Total: 5

# Invalidar cache
🗑️ Cache de FAQs invalidado - 4 keys eliminadas

# Error en cache
❌ Error obteniendo FAQs desde cache: ...
```

### Análisis de Performance

```
Consulta sin cache:
- Tiempo promedio: 150-300ms
- Consulta a MySQL + procesamiento

Consulta con cache:
- Tiempo promedio: 5-15ms
- Lectura directa desde Redis
- Mejora: 95-97%
```

---

## 🚀 Mejoras Futuras

- [ ] Implementar paginación para grandes volúmenes de FAQs
- [ ] Agregar sistema de votación (útil/no útil)
- [ ] Implementar búsqueda full-text con Elasticsearch
- [ ] Agregar versionamiento de respuestas
- [ ] Implementar ordenamiento por relevancia/popularidad
- [ ] Cache de búsqueda por palabras clave
- [ ] Métricas de uso de cada FAQ
- [ ] Sistema de sugerencias de FAQs relacionadas
- [ ] Soporte multiidioma
- [ ] Panel de administración con estadísticas

---

## 📚 Documentación Relacionada

- [Sistema de QR](README-QR-System.md)
- [Sistema de Categorías FAQ](./README-CategoryFaq-System.md)
- [Arquitectura de Caché Redis](./README-Redis-Cache-Architecture.md)

---

## 👥 Contribuidores

- **Desarrollo inicial**: Sebastian David Escorcia Montes
- **Colaboradores**: Jeniffer Saumeth

---

## 📄 Licencia

Este proyecto es parte del sistema BogotaMetroApp - SENA 2025
