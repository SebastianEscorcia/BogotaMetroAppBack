# 🚇 BogotaMetroApp — Backend

Sistema backend para la aplicación del Metro de Bogotá, desarrollado como proyecto académico del programa Tecnólogo en Análisis y Desarrollo de Software (ADSO) del SENA.

## 📋 Descripción

Este es el **backend** de la aplicación del Metro de Bogotá, una solución completa para la gestión del sistema de transporte metro de la ciudad. La aplicación permite administrar estaciones, líneas, rutas, viajes, pagos y la gestión de pasajeros mediante códigos QR.

El proyecto es una iniciativa académica desarrollada en el **SENA (Servicio Nacional de Aprendizaje)** como parte del programa Tecnólogo en Análisis y Desarrollo de Software, con el objetivo de crear un sistema robusto y escalable para el futuro sistema de transporte metro de Bogotá.

## 🚀 Tecnologías Utilizadas

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-0.12.5-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8+-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-2.7.0-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Lombok](https://img.shields.io/badge/Lombok-BC4521?style=for-the-badge&logo=lombok&logoColor=white)

### Stack Tecnológico

- **Java 21** — Lenguaje de programación principal
- **Spring Boot 3.5.7** — Framework de aplicación
- **Spring Security** — Gestión de autenticación y autorización
- **JWT (jjwt 0.12.5)** — Tokens de autenticación JSON Web Token
- **Spring Data JPA** — Capa de persistencia de datos
- **MySQL** — Base de datos relacional
- **Swagger/OpenAPI (springdoc-openapi 2.7.0)** — Documentación de API
- **Lombok** — Reducción de código boilerplate
- **Bean Validation** — Validación de datos
- **Gradle** — Sistema de construcción y gestión de dependencias

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una **arquitectura en capas** bien definida, organizada en el paquete base `com.sena.BogotaMetroApp`:

```
src/main/java/com/sena/BogotaMetroApp/
├── BogotaMetroAppApplication.java          # Clase principal de Spring Boot
├── components/                              # Componentes de Spring
│   └── DataSeeder.java                     # Inicializador de datos
├── configuration/                           # Configuraciones de la aplicación
│   ├── app/                                # Configuración general
│   └── security/                           # Configuración de seguridad
├── presentation/                            # Capa de presentación
│   ├── controller/                         # Controladores REST
│   │   ├── authentication/                 # Autenticación
│   │   ├── conexion/                       # Conexiones entre estaciones
│   │   ├── interrupcion/                   # Interrupciones del servicio
│   │   ├── pasajero/                       # Gestión de pasajeros
│   │   ├── pasajeroviaje/                  # Relación pasajero-viaje
│   │   ├── pasarela/                       # Pasarelas de pago
│   │   ├── pago/                           # Pagos
│   │   ├── puntointeres/                   # Puntos de interés
│   │   ├── qr/                             # Códigos QR
│   │   ├── soporte/                        # Soporte técnico
│   │   ├── viajeruta/                      # Rutas de viaje
│   │   ├── EstacionController.java         # Estaciones
│   │   ├── LineaController.java            # Líneas del metro
│   │   ├── RutaController.java             # Rutas
│   │   └── ViajeController.java            # Viajes
│   └── dto/                                # Data Transfer Objects
│       ├── request/                        # DTOs de entrada
│       └── response/                       # DTOs de salida
├── services/                                # Capa de lógica de negocio
│   ├── [recurso]/                          # Interfaces de servicios
│   └── implementation/                     # Implementaciones de servicios
├── persistence/                             # Capa de persistencia
│   ├── models/                             # Entidades JPA
│   └── repository/                         # Repositorios Spring Data
├── mapper/                                  # Mappers de entidades a DTOs
├── utils/                                   # Utilidades y helpers
└── errors/                                  # Manejo de errores y excepciones
```

### Capas de la Arquitectura

1. **Presentation Layer** (`presentation/`) — Controladores REST y DTOs para la comunicación con el cliente
2. **Service Layer** (`services/`) — Lógica de negocio e implementaciones de servicios
3. **Persistence Layer** (`persistence/`) — Entidades JPA y repositorios para acceso a datos
4. **Configuration Layer** (`configuration/`) — Configuraciones de seguridad y aplicación

## 🌐 API Endpoints

La API REST está organizada en los siguientes endpoints principales:

| Recurso | Endpoint Base | Descripción |
|---------|--------------|-------------|
| **Autenticación** | `/api/auth` | Login, registro y gestión de tokens JWT |
| **Estaciones** | `/api/estaciones` | CRUD de estaciones del metro |
| **Líneas** | `/api/lineas` | Gestión de líneas del metro |
| **Rutas** | `/api/rutas` | Administración de rutas entre estaciones |
| **Viajes** | `/api/viajes` | Gestión de viajes y trayectos |
| **Pasajero-Viaje** | `/api/pasajero-viaje` | Relación entre pasajeros y viajes |
| **Pagos** | `/api/pagos` | Procesamiento de pagos |
| **Pasarelas de Pago** | `/api/pasarelas` | Configuración de pasarelas de pago |
| **Códigos QR** | `/api/qr` | Generación y validación de códigos QR |
| **Conexiones** | `/api/conexiones` | Conexiones entre estaciones |
| **Interrupciones** | `/api/interrupciones` | Gestión de interrupciones del servicio |
| **Puntos de Interés** | `/api/poi` | Puntos de interés cercanos a estaciones |
| **Soporte** | `/api/soporte` | Sistema de tickets de soporte |

## 📋 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **Java 21** o superior
- **MySQL 8+**
- **Gradle 8+** (incluido en el wrapper del proyecto)
- **Git** para clonar el repositorio

## 🔧 Instalación y Ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/SebastianEscorcia/BogotaMetroAppBack.git
cd BogotaMetroAppBack
```

### 2. Configurar la base de datos

Crea una base de datos MySQL para el proyecto:

```sql
CREATE DATABASE bogota_metro_db;
CREATE USER 'metro_user'@'localhost' IDENTIFIED BY 'tu_contraseña_segura';
GRANT ALL PRIVILEGES ON bogota_metro_db.* TO 'metro_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configurar application.properties

Crea o edita el archivo `src/main/resources/application.properties` con tu configuración:

```properties
# Configuración de la base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/bogota_metro_db
spring.datasource.username=metro_user
spring.datasource.password=tu_contraseña_segura
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Configuración JWT
jwt.secret=tu_clave_secreta_muy_larga_y_segura_de_al_menos_256_bits
jwt.expiration=86400000

# Configuración del servidor
server.port=8080

# Configuración de Swagger
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### 4. Ejecutar la aplicación

#### Usando Gradle Wrapper (recomendado):

```bash
./gradlew bootRun
```

#### En Windows:

```bash
gradlew.bat bootRun
```

#### Compilar y ejecutar JAR:

```bash
./gradlew build
java -jar build/libs/BogotaMetroApp-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en `http://localhost:8080`

## ⚙️ Configuración

### Variables de Entorno Requeridas

Puedes usar variables de entorno en lugar de configurar directamente en `application.properties`:

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `DB_URL` | URL de conexión a MySQL | `jdbc:mysql://localhost:3306/bogota_metro_db` |
| `DB_USERNAME` | Usuario de la base de datos | `metro_user` |
| `DB_PASSWORD` | Contraseña de la base de datos | `tu_contraseña_segura` |
| `JWT_SECRET` | Clave secreta para firmar JWT | `clave_secreta_muy_larga_y_segura` |
| `JWT_EXPIRATION` | Tiempo de expiración del token (ms) | `86400000` (24 horas) |
| `SERVER_PORT` | Puerto del servidor | `8080` |

### Ejemplo con variables de entorno

```bash
export DB_URL=jdbc:mysql://localhost:3306/bogota_metro_db
export DB_USERNAME=metro_user
export DB_PASSWORD=tu_contraseña_segura
export JWT_SECRET=tu_clave_secreta_jwt
./gradlew bootRun
```

## 📚 Documentación de la API

La documentación interactiva de la API está disponible mediante **Swagger UI** una vez que la aplicación esté en ejecución:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **API Docs (JSON)**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

Swagger UI proporciona:
- Documentación completa de todos los endpoints
- Posibilidad de probar las APIs directamente desde el navegador
- Esquemas de request/response
- Códigos de estado HTTP y ejemplos

## 🔗 Repositorio Frontend

Este backend se complementa con una aplicación frontend desarrollada en tecnologías web modernas:

🔗 **Frontend Repository**: [BogotaMetroAppFrontend](https://github.com/SebastianEscorcia/BogotaMetroAppFrontend)

## 👥 Autores

Este proyecto fue desarrollado por estudiantes del programa ADSO del SENA:

- **Jennifer Saumeth Gómez**
- **Diana Carolina López Lezama**
- **Sebastian David Escorcia Montes** - [GitHub](https://github.com/SebastianEscorcia)

## 📄 Licencia

Este proyecto es un trabajo académico desarrollado en el **SENA** (Servicio Nacional de Aprendizaje).

---

⭐ **Proyecto Académico SENA - Tecnólogo ADSO** | 🚇 Metro de Bogotá
