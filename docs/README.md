# 📚 Documentación Técnica - BogotaMetroApp

Bienvenido a la documentación técnica del sistema BogotaMetroApp. Este directorio contiene documentación detallada de los diferentes módulos y arquitecturas del sistema.

---

## 📖 Índice de Documentación

### 🏗️ Arquitectura General

- **[Arquitectura de Caché Redis](./README-Redis-Cache-Architecture.md)**
  - Descripción completa de la arquitectura de caché distribuida
  - `AbstractRedisCacheService` y sus implementaciones
  - Patrones de diseño y mejores prácticas
  - Configuración y monitoreo

---

### 🎫 Sistemas Principales

#### 1. Sistema de Códigos QR

📄 **[Sistema de QR](README-QR-System.md)**

Sistema de generación y validación de códigos QR para control de acceso al metro.

**Características:**
- Generación de QR únicos por usuario
- Caché Redis con TTL dinámico
- Versionamiento optimista
- Job de limpieza automática
- Histórico de QR no utilizados

**Endpoints principales:**
- `POST /api/qr/acceso` - Generar QR
- `POST /api/qr/validar` - Validar QR

---

#### 2. Sistema de Categorías FAQ

📄 **[Sistema de Categorías FAQ](./README-CategoryFaq-System.md)**

Sistema de gestión de categorías para organizar las preguntas frecuentes.

**Características:**
- CRUD completo de categorías
- Caché Redis de 24 horas
- Soft delete (borrado lógico)
- Relación uno-a-muchos con FAQs

**Endpoints principales:**
- `GET /api/support/categories` - Listar categorías
- `POST /api/support/categories` - Crear categoría
- `PUT /api/support/categories/{id}` - Actualizar categoría
- `DELETE /api/support/categories/{id}` - Eliminar categoría

---

#### 3. Sistema de Support FAQ

📄 **[Sistema de Support FAQ](./README-SupportFaq-System.md)**

Sistema de gestión de preguntas frecuentes con soporte para categorización.

**Características:**
- CRUD completo de FAQs
- Caché multi-nivel (global y por categoría)
- Filtrado por categoría
- Soporte para respuestas largas (TEXT/LOB)
- Invalidación en cascada

**Endpoints principales:**
- `GET /api/support/faqs` - Listar todas las FAQs
- `GET /api/support/faqs/category/{id}` - FAQs por categoría
- `POST /api/support/faqs` - Crear FAQ
- `PUT /api/support/faqs/{id}` - Actualizar FAQ
- `DELETE /api/support/faqs/{id}` - Eliminar FAQ

---

## 🗂️ Estructura de la Documentación

```
docs/
├── README.md                              # Este archivo (índice)
├── README-Redis-Cache-Architecture.md     # Arquitectura de caché
├── README-CategoryFaq-System.md           # Sistema de categorías FAQ
└── README-SupportFaq-System.md            # Sistema de FAQs
```

```
raíz/
└── README-QR-System.md                    # Sistema de QR
```

---

## 🏛️ Arquitectura General del Sistema

```
┌─────────────────────────────────────────────────────────────────┐
│                      BOGOTA METRO APP                            │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                           │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐                │
│  │ QR         │  │ CategoryFaq│  │ SupportFaq │                │
│  │ Controller │  │ Controller │  │ Controller │                │
│  └────────────┘  └────────────┘  └────────────┘                │
└─────────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                               │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐                │
│  │ QR         │  │ CategoryFaq│  │ SupportFaq │                │
│  │ Service    │  │ Service    │  │ Service    │                │
│  └────────────┘  └────────────┘  └────────────┘                │
└─────────────────────────────────────────────────────────────────┘
                            │
            ┌───────────────┼───────────────┐
            ▼               ▼               ▼
┌─────────────────┐  ┌─────────────────────────────────┐
│                 │  │   CACHE SERVICE LAYER           │
│  REPOSITORY     │  │  ┌──────────────────────────┐   │
│  LAYER          │  │  │ AbstractRedisCacheService│   │
│  (JPA/MySQL)    │  │  └────────────┬─────────────┘   │
│                 │  │               │                 │
│  ┌──────────┐   │  │  ┌────────────┴────────────┐   │
│  │ Qr       │   │  │  │ 3 Implementaciones:     │   │
│  │ Repo     │   │  │  │ • QrRedisCacheService   │   │
│  ├──────────┤   │  │  │ • CategoryFaqCache      │   │
│  │Category  │   │  │  │ • FaqRedisCache         │   │
│  │FaqRepo   │   │  │  └─────────────────────────┘   │
│  ├──────────┤   │  └─────────────────────────────────┘
│  │SupportFaq│   │
│  │Repo      │   │
│  └──────────┘   │
└─────────────────┘
        │                           │
        ▼                           ▼
┌─────────────────┐       ┌─────────────────┐
│     MySQL       │       │     Redis       │
│   (Persistent)  │       │    (Cache)      │
└─────────────────┘       └─────────────────┘
```

---

## 🔑 Conceptos Clave

### Cache-Aside Pattern

Todos los sistemas implementan el patrón **Cache-Aside**:

1. **Lectura**:
   - Intentar leer del cache
   - Si hay MISS → Leer de DB → Guardar en cache → Retornar
   - Si hay HIT → Retornar directamente

2. **Escritura**:
   - Escribir en DB
   - Invalidar cache
   - Próxima lectura recarga el cache

### Soft Delete

Todos los sistemas usan **borrado lógico**:
- No se eliminan registros físicamente
- Se marca como `active = false` o `isActive = false`
- Los queries filtran por registros activos
- Permite auditoría y recuperación

### Serialización

Los DTOs de respuesta deben implementar `Serializable`:
```java
public class CategoryFaqResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    // ...
}
```

---

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17+ | Lenguaje principal |
| Spring Boot | 3.x | Framework backend |
| MySQL | 8.x | Base de datos principal |
| Redis | 7.x | Caché distribuida |
| JPA/Hibernate | - | ORM |
| Lombok | - | Reducción de boilerplate |
| MapStruct | - | Mapeo de objetos |
| Docker | - | Contenerización |

---

## 📊 Métricas de Performance

### Sin Caché

| Operación | Tiempo Promedio |
|-----------|----------------|
| Consulta QR | 150-250ms |
| Consulta Categorías | 100-200ms |
| Consulta FAQs | 150-300ms |

### Con Caché

| Operación | Tiempo Promedio | Mejora |
|-----------|----------------|--------|
| Consulta QR | 5-15ms | 95-97% |
| Consulta Categorías | 3-10ms | 95-97% |
| Consulta FAQs | 5-15ms | 95-97% |

---

## 🔧 Configuración del Entorno

### Variables de Entorno Requeridas

```bash
# Database
DB_URL=jdbc:mysql://localhost:3306/bogota_metro
DB_USERNAME=root
DB_PASSWORD=password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=redis_password
```

### Docker Compose

Para levantar todos los servicios:

```bash
docker-compose up -d
```

---

## 🧪 Testing

### Verificar Redis

```bash
# Conectar a Redis
redis-cli -h localhost -p 6379 -a redis_password

# Verificar conexión
PING
# Respuesta: PONG

# Ver todas las claves
KEYS *

# Ver claves específicas
KEYS usuario:*
KEYS supportfaqs:*
KEYS categoryfaqs:*
```

### Verificar MySQL

```bash
# Conectar a MySQL
mysql -h localhost -u root -p

# Ver bases de datos
SHOW DATABASES;

# Usar la base de datos
USE bogota_metro;

# Ver tablas
SHOW TABLES;
```

---

## 📝 Convenciones de Código

### Nomenclatura de Clases

- **Entidades**: `CategoryFaq`, `SupportFaq`, `Qr`
- **DTOs Request**: `CategoryFaqRequestDTO`, `SupportFaqRequestDTO`
- **DTOs Response**: `CategoryFaqResponseDTO`, `SupportFaqResponseDTO`
- **Services**: `CategoryFaqServiceImpl`, `SupportFaqServiceImpl`
- **Repositories**: `CategoryFaqRepository`, `SupportFaqRepository`
- **Cache Services**: `CategoryFaqRedisCacheServiceImpl`, `FaqRedisCacheServiceImpl`

### Nomenclatura de Endpoints

```
/api/{modulo}/{recurso}

Ejemplos:
/api/qr/acceso
/api/support/categories
/api/support/faqs
```

### Logging

```java
// ✅ Usar emojis para facilitar la lectura
log.info("✅ Cache HIT - datos obtenidos desde Redis");
log.info("💾 Datos guardados en cache");
log.info("🗑️ Cache invalidado");
log.error("❌ Error en operación: {}", mensaje, exception);
```

---

## 🚀 Próximos Pasos

### Mejoras Planificadas

1. **Monitoreo**
   - Integrar Prometheus + Grafana
   - Métricas de cache hit/miss rate
   - Alertas automáticas

2. **Testing**
   - Aumentar cobertura de pruebas unitarias
   - Implementar pruebas de integración con Testcontainers
   - Pruebas de carga con JMeter

3. **Escalabilidad**
   - Implementar Redis Cluster
   - Redis Sentinel para alta disponibilidad
   - Sharding de base de datos

4. **Seguridad**
   - Rate limiting con Redis
   - JWT token refresh con Redis
   - Auditoría completa de operaciones

5. **Documentación**
   - Swagger/OpenAPI completo
   - Ejemplos de uso con Postman
   - Guías de troubleshooting

---

## 🤝 Contribuir

Para contribuir a la documentación:

1. Seguir el formato Markdown establecido
2. Incluir ejemplos de código cuando sea relevante
3. Agregar diagramas para conceptos complejos
4. Mantener la consistencia con la documentación existente
5. Actualizar este índice si se agregan nuevos documentos

---

## 📞 Soporte

Para preguntas o problemas:

- **Equipo de desarrollo**: BogotaMetroApp Team
- **Repositorio**: [GitHub - BogotaMetroApp]
- **Documentación adicional**: Ver archivos individuales en este directorio

---

## 👥 Autores

- **Sebastian David Escorcia Montes** - Desarrollo inicial y arquitectura
- **Jeniffer Saumeth** - Colaboradora

---

## 📄 Licencia

Este proyecto es parte del sistema BogotaMetroApp - SENA 2025

---

**Última actualización**: Enero 2026
