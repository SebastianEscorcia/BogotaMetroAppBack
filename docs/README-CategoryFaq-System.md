# 📚 Sistema de Categorías FAQ con Redis Cache

Sistema de gestión de categorías para preguntas frecuentes (FAQs), implementado con Spring Boot, MySQL y Redis para caché de alta velocidad.

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

- ✅ CRUD completo de categorías de FAQs
- 🚀 Caché en Redis para consultas ultrarrápidas
- 🔄 Invalidación automática de caché en modificaciones
- 🗑️ Soft delete (borrado lógico)
- 🏗️ Arquitectura basada en `AbstractRedisCacheService`
- 📊 Relación uno-a-muchos con Support FAQs
- 🔒 Validación de datos con Spring Validation

---

## 🏛️ Arquitectura

### Arquitectura General

```
┌──────────────────┐     ┌──────────────────┐     ┌──────────────────┐
│                  │     │                  │     │                  │
│   Controller     │────▶│    Service       │────▶│   Repository     │
│  CategoryFaq     │     │  CategoryFaq     │     │  CategoryFaq     │
│                  │     │                  │     │                  │
└──────────────────┘     └────────┬─────────┘     └──────────────────┘
                                  │                         │
                                  ▼                         ▼
                         ┌──────────────────┐     ┌──────────────────┐
                         │ CategoryFaqCache │     │                  │
                         │    (Redis)       │     │      MySQL       │
                         │                  │     │                  │
                         └──────────────────┘     └──────────────────┘
```

### Arquitectura de Caché

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
    │ CategoryFaqRedisCacheServiceImpl│
    │ - ICategoryFaqCacheService     │
    │                                │
    │  Métodos:                      │
    │  • getCachedCategoryFaqs()     │
    │  • cacheCategoryFaqs()         │
    │  • invalidateCategoryFaqsCache()│
    └────────────────────────────────┘
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

### Entidad CategoryFaq

```java
@Entity
@Table(name = "category_faqs")
public class CategoryFaq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Column(name = "is_active")
    private boolean active = true;
    
    @OneToMany(mappedBy = "categoryFaq", 
               cascade = CascadeType.ALL, 
               orphanRemoval = true, 
               fetch = FetchType.LAZY)
    private List<SupportFaq> supportFaqs = new ArrayList<>();
}
```

### DTOs

**CategoryFaqRequestDTO:**
```java
public class CategoryFaqRequestDTO {
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;
}
```

**CategoryFaqResponseDTO:**
```java
public class CategoryFaqResponseDTO implements Serializable {
    private Long id;
    private String name;
    private boolean active;
}
```

### Tabla MySQL

```sql
CREATE TABLE category_faqs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_active (is_active)
);
```

---

## 📁 Estructura de Archivos

```
src/main/java/com/sena/BogotaMetroApp/
├── externalservices/cache/
│   ├── AbstractRedisCacheService.java              # Clase base abstracta
│   ├── ICategoryFaqCacheService.java               # Interface del servicio de caché
│   └── CategoryFaqRedisCacheServiceImpl.java       # Implementación del servicio de caché
├── mapper/supportfaq/
│   └── CategoryFaqMapper.java                      # Mapeo entre entidades y DTOs
├── persistence/
│   ├── models/supportfaq/
│   │   └── CategoryFaq.java                        # Entidad principal
│   └── repository/supportfaq/
│       └── CategoryFaqRepository.java              # Repositorio JPA
├── presentation/
│   ├── controller/supportfaq/
│   │   └── CategoryFaqController.java              # Controlador REST
│   └── dto/supportfaq/
│       ├── CategoryFaqRequestDTO.java              # DTO de entrada
│       └── CategoryFaqResponseDTO.java             # DTO de salida
└── services/supportfaq/
    ├── ICategoryFaqService.java                    # Interface del servicio
    └── CategoryFaqServiceImpl.java                 # Implementación del servicio
```

---

## 🔄 Flujo de Funcionamiento

### Consulta de Categorías (GET)

```
1. Usuario solicita categorías
         │
         ▼
2. ¿Existe en Redis Cache?
    │           │
   SÍ          NO
    │           │
    ▼           ▼
3. Retornar   4. Consultar MySQL
   del cache      │
                  ▼
             5. Guardar en cache
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
3. Ejecutar operación en MySQL
         │
         ▼
4. Invalidar cache de Redis
         │
         ▼
5. Retornar respuesta (Response DTO)
```

### Código del Flujo Principal

```java
@Override
@Transactional(readOnly = true)
public List<CategoryFaqResponseDTO> getAllActiveCategoryFaqs() {
    // 1. Intentar obtener desde cache
    return categoryCacheService.getCachedCategoryFaqs()
            .orElseGet(() -> {
                // 2. Cache miss - consultar DB
                log.info("Cache miss - consultando Category FAQs desde DB");
                List<CategoryFaqResponseDTO> categories = 
                    catFaqRepository.findAllByActiveTrue()
                        .stream()
                        .map(catFaqMapper::toDto)
                        .collect(Collectors.toList());
                
                // 3. Guardar en cache
                categoryCacheService.cacheCategoryFaqs(categories);
                
                return categories;
            });
}
```

---

## 🌐 API Endpoints

### Crear Categoría

```http
POST /api/support/categories
Content-Type: application/json
Authorization: Bearer {token}

{
    "name": "Información General"
}
```

**Response (201 Created):**
```json
{
    "id": 1,
    "name": "Información General",
    "active": true
}
```

---

### Obtener Todas las Categorías Activas

```http
GET /api/support/categories
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
[
    {
        "id": 1,
        "name": "Información General",
        "active": true
    },
    {
        "id": 2,
        "name": "Tarifas y Pagos",
        "active": true
    }
]
```

---

### Obtener Categoría por ID

```http
GET /api/support/categories/{id}
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
    "id": 1,
    "name": "Información General",
    "active": true
}
```

---

### Actualizar Categoría

```http
PUT /api/support/categories/{id}
Content-Type: application/json
Authorization: Bearer {token}

{
    "name": "Información General Actualizada"
}
```

**Response (200 OK):**
```json
{
    "id": 1,
    "name": "Información General Actualizada",
    "active": true
}
```

---

### Eliminar Categoría (Soft Delete)

```http
DELETE /api/support/categories/{id}
Authorization: Bearer {token}
```

**Response (204 No Content)**

---

## 💾 Caché Redis

### Estructura de Keys

| Key Pattern | Descripción | TTL | Ejemplo |
|-------------|-------------|-----|---------|
| `categoryfaqs:all` | Lista de todas las categorías activas | 24 horas | `categoryfaqs:all` |

### Datos Almacenados

```json
// Key: categoryfaqs:all
[
    {
        "id": 1,
        "name": "Información General",
        "active": true
    },
    {
        "id": 2,
        "name": "Tarifas y Pagos",
        "active": true
    }
]
```

### Implementación del Cache Service

```java
@Service
@Slf4j
public class CategoryFaqRedisCacheServiceImpl 
        extends AbstractRedisCacheService 
        implements ICategoryFaqCacheService {
    
    private static final String CATEGORY_FAQ_KEY = "categoryfaqs:all";
    private static final Duration CACHE_TTL = Duration.ofHours(24);
    
    @Override
    public Optional<List<CategoryFaqResponseDTO>> getCachedCategoryFaqs() {
        try {
            Object cached = redisTemplate.opsForValue().get(CATEGORY_FAQ_KEY);
            if (cached instanceof List<?>) {
                log.info("✅ Cache HIT - Categorías obtenidas desde Redis");
                return Optional.of((List<CategoryFaqResponseDTO>) cached);
            }
        } catch (Exception e) {
            log.error("❌ Error obteniendo categorías desde cache", e);
        }
        return Optional.empty();
    }
    
    @Override
    public void cacheCategoryFaqs(List<CategoryFaqResponseDTO> categories) {
        try {
            setWithExpiry(CATEGORY_FAQ_KEY, categories, CACHE_TTL);
            log.info("💾 Categorías guardadas en cache - Total: {}", categories.size());
        } catch (Exception e) {
            log.error("❌ Error guardando categorías en cache", e);
        }
    }
    
    @Override
    public void invalidateCategoryFaqsCache() {
        try {
            delete(CATEGORY_FAQ_KEY);
            log.info("🗑️ Cache de categorías invalidado");
        } catch (Exception e) {
            log.error("❌ Error invalidando cache de categorías", e);
        }
    }
}
```

---

## 🔧 Implementación del Servicio

### Interface del Servicio

```java
public interface ICategoryFaqService {
    CategoryFaqResponseDTO createCategoryFaq(CategoryFaqRequestDTO dto);
    CategoryFaqResponseDTO getCategoryFaqById(Long id);
    List<CategoryFaqResponseDTO> getAllActiveCategoryFaqs();
    CategoryFaqResponseDTO updateCategoryFaq(Long id, CategoryFaqRequestDTO dto);
    void deleteCategoryFaq(Long id);
}
```

### Implementación del Servicio

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryFaqServiceImpl implements ICategoryFaqService {
    private final CategoryFaqRepository catFaqRepository;
    private final CategoryFaqMapper catFaqMapper;
    private final ICategoryFaqCacheService categoryCacheService;
    
    @Override
    @Transactional
    public CategoryFaqResponseDTO createCategoryFaq(CategoryFaqRequestDTO dto) {
        CategoryFaq categoryFaq = catFaqMapper.toEntity(dto);
        categoryFaq.setActive(true);
        CategoryFaq saved = catFaqRepository.save(categoryFaq);
        
        // Invalidar cache después de crear
        categoryCacheService.invalidateCategoryFaqsCache();
        
        return catFaqMapper.toDto(saved);
    }
    
    @Override
    @Transactional
    public void deleteCategoryFaq(Long id) {
        CategoryFaq categoryFaq = catFaqRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "CategoryFaq no encontrada con id: " + id));
        
        // Soft delete
        categoryFaq.setActive(false);
        catFaqRepository.save(categoryFaq);
        
        // Invalidar cache
        categoryCacheService.invalidateCategoryFaqsCache();
    }
}
```

---

## 🐛 Solución de Problemas

### Problema: Cache no se actualiza después de modificaciones

**Causa:** La invalidación del cache no se está ejecutando correctamente.

**Solución:** Verificar que `invalidateCategoryFaqsCache()` se llame después de cada operación de escritura:

```java
// ✅ Correcto
@Transactional
public CategoryFaqResponseDTO updateCategoryFaq(Long id, CategoryFaqRequestDTO dto) {
    // ... lógica de actualización ...
    CategoryFaq updated = catFaqRepository.save(categoryFaq);
    categoryCacheService.invalidateCategoryFaqsCache(); // ← Invalidar cache
    return catFaqMapper.toDto(updated);
}
```

---

### Problema: Cache retorna datos null

**Causa:** El DTO no implementa `Serializable`.

**Solución:** Asegurar que el DTO sea serializable:

```java
public class CategoryFaqResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    // ... campos ...
}
```

---

### Problema: Soft delete no funciona

**Causa:** La consulta no filtra por `active = true`.

**Solución:** Usar el método correcto del repositorio:

```java
// ❌ Incorrecto
Optional<CategoryFaq> findById(Long id);

// ✅ Correcto
Optional<CategoryFaq> findByIdAndActiveTrue(Long id);
```

---

### Verificar Cache en Redis

```bash
# Conectar a Redis CLI
redis-cli

# Ver clave de categorías
GET categoryfaqs:all

# Ver TTL
TTL categoryfaqs:all

# Limpiar cache manualmente
DEL categoryfaqs:all
```

---

## 📊 Métricas y Logs

### Logs Importantes

```
# Cache hit
✅ Cache HIT - Categorías obtenidas desde Redis

# Cache miss
Cache miss - consultando Category FAQs desde DB

# Guardar en cache
💾 Categorías guardadas en cache - Total: 5

# Invalidar cache
🗑️ Cache de categorías invalidado

# Error en cache
❌ Error obteniendo categorías desde cache: ...
```

---

## 🚀 Mejoras Futuras

- [ ] Implementar paginación para grandes volúmenes de categorías
- [ ] Agregar versionamiento de categorías
- [ ] Implementar ordenamiento personalizado
- [ ] Cache por categoría individual (además del cache de lista completa)
- [ ] Métricas de uso de cache (hit rate, miss rate)
- [ ] Integrar con sistema de búsqueda (Elasticsearch)

---

## 📚 Documentación Relacionada

- [Sistema de QR](README-QR-System.md)
- [Sistema de Support FAQ](./README-SupportFaq-System.md)
- [Arquitectura de Caché Redis](./README-Redis-Cache-Architecture.md)

---

## 👥 Contribuidores

- **Desarrollo inicial**: Sebastian David Escorcia Montes
- **Colaboradores**: Jeniffer Saumeth, Gabriel Polo

---

## 📄 Licencia

Este proyecto es parte del sistema BogotaMetroApp - SENA 2025
