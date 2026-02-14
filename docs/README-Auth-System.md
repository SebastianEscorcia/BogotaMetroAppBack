# Sistema de Autenticación y Autorización

## Índice
1. [Conceptos Fundamentales](#conceptos-fundamentales)
2. [Arquitectura del Sistema](#arquitectura-del-sistema)
3. [Flujo de Autenticación](#flujo-de-autenticación)
4. [Componentes del Sistema](#componentes-del-sistema)
5. [Configuración de Seguridad](#configuración-de-seguridad)
6. [Endpoints de Autenticación](#endpoints-de-autenticación)

---

## Conceptos Fundamentales

### ¿Qué es Autenticación?
La **autenticación** es el proceso de verificar la identidad de un usuario. Es responder a la pregunta: *"¿Quién eres?"*

En este sistema, la autenticación se realiza mediante:
- **Correo electrónico**: Identificador único del usuario
- **Contraseña**: Credencial secreta verificada con BCrypt

### ¿Qué es Autorización?
La **autorización** es el proceso de determinar qué puede hacer un usuario autenticado. Es responder a la pregunta: *"¿Qué tienes permitido hacer?"*

En este sistema, la autorización se basa en:
- **Roles**: ADMIN, PASAJERO, SOPORTE, OPERADOR
- **Anotaciones**: `@PreAuthorize` para proteger endpoints específicos

### ¿Qué es JWT (JSON Web Token)?
Un **JWT** es un estándar abierto (RFC 7519) para transmitir información de forma segura entre partes como un objeto JSON firmado digitalmente.

```
┌─────────────────────────────────────────────────────────────┐
│                         JWT Token                           │
├─────────────────┬─────────────────┬─────────────────────────┤
│     HEADER      │     PAYLOAD     │       SIGNATURE         │
│   (Algoritmo)   │   (Datos)       │   (Firma Digital)       │
├─────────────────┼─────────────────┼─────────────────────────┤
│ {               │ {               │ HMACSHA256(             │
│   "alg":"HS256",│   "sub":"email",│   base64(header) + "."  │
│   "typ":"JWT"   │   "rol":"ADMIN",│   + base64(payload),    │
│ }               │   "exp":123456  │   secret                │
│                 │ }               │ )                       │
└─────────────────┴─────────────────┴─────────────────────────┘
```

**Estructura del JWT en este sistema:**
- `sub` (subject): Correo del usuario
- `rol`: Rol del usuario
- `iat` (issued at): Fecha de creación
- `exp` (expiration): Fecha de expiración (10 horas)

---

## Arquitectura del Sistema

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   Frontend   │────▶│  JwtFilter   │────▶│  Controller  │
│   (React)    │     │  (Filtro)    │     │  (Endpoint)  │
└──────────────┘     └──────────────┘     └──────────────┘
       │                    │                    │
       │                    ▼                    │
       │           ┌──────────────┐              │
       │           │SecurityContext│             │
       │           │   Holder     │◀─────────────┘
       │           └──────────────┘
       │                    │
       ▼                    ▼
┌──────────────┐     ┌──────────────┐
│   Cookie     │     │   Usuario    │
│ (HttpOnly)   │     │ Autenticado  │
└──────────────┘     └──────────────┘
```

---

## Flujo de Autenticación

### 1. Login (Inicio de Sesión)

```
┌─────────┐                    ┌─────────────┐                    ┌────────────┐
│ Cliente │                    │AuthController│                    │AuthService │
└────┬────┘                    └──────┬──────┘                    └─────┬──────┘
     │                                │                                  │
     │ POST /api/auth/login           │                                  │
     │ {correo, clave}                │                                  │
     │───────────────────────────────▶│                                  │
     │                                │                                  │
     │                                │ login(request)                   │
     │                                │─────────────────────────────────▶│
     │                                │                                  │
     │                                │                    Verificar usuario
     │                                │                    Validar contraseña
     │                                │                    Generar JWT
     │                                │                                  │
     │                                │◀─────────────────────────────────│
     │                                │ AuthResponse(token, rol)         │
     │                                │                                  │
     │      Set-Cookie: accessToken   │                                  │
     │◀───────────────────────────────│                                  │
     │      Body: {rol}               │                                  │
     │                                │                                  │
```

### 2. Petición Autenticada

```
┌─────────┐                    ┌─────────────┐                    ┌────────────┐
│ Cliente │                    │  JwtFilter  │                    │ Controller │
└────┬────┘                    └──────┬──────┘                    └─────┬──────┘
     │                                │                                  │
     │ GET /api/protected             │                                  │
     │ Cookie: accessToken=xxx        │                                  │
     │───────────────────────────────▶│                                  │
     │                                │                                  │
     │                                │ 1. Extraer token de cookie       │
     │                                │ 2. Validar JWT                   │
     │                                │ 3. Extraer correo                │
     │                                │ 4. Buscar usuario en BD          │
     │                                │ 5. Crear Authentication          │
     │                                │ 6. Guardar en SecurityContext    │
     │                                │                                  │
     │                                │─────────────────────────────────▶│
     │                                │                                  │
     │◀──────────────────────────────────────────────────────────────────│
     │      Respuesta                 │                                  │
```

---

## Componentes del Sistema

### JwtServices
**Ubicación:** `services/JwtServices.java`

Servicio responsable de la generación y validación de tokens JWT.

```java
@Service
public class JwtServices {
    // Genera un token JWT con el correo y rol del usuario
    public String generateToken(Usuario usuario);
    
    // Extrae el correo (subject) del token
    public String extraerCorreo(String token);
}
```

**Configuración requerida en `application.properties`:**
```properties
app.security.jwt-key=TuClaveSecretaMuyLargaYSeguraDeAlMenos256Bits
```

### JwtFilter
**Ubicación:** `configuration/security/JwtFilter.java`

Filtro que intercepta todas las peticiones HTTP para validar el JWT.

**Responsabilidades:**
1. Extraer el token del header `Authorization` o cookie `accessToken`
2. Validar el token
3. Crear el objeto `Authentication`
4. Almacenar la autenticación en `SecurityContextHolder`

**Flujo de resolución del token:**
```
┌─────────────────────────────────────────────────────────┐
│                    resolveToken()                        │
├─────────────────────────────────────────────────────────┤
│ 1. Buscar en Header: Authorization: Bearer <token>       │
│    └─▶ Si existe, retornar token                        │
│                                                          │
│ 2. Buscar en Cookies: accessToken=<token>               │
│    └─▶ Si existe, retornar token                        │
│                                                          │
│ 3. Si no hay token, retornar null                       │
└─────────────────────────────────────────────────────────┘
```

### AuthController
**Ubicación:** `presentation/controller/authentication/AuthController.java`

Controlador REST que expone los endpoints de autenticación.

### AuthServiceImpl
**Ubicación:** `services/auth/AuthServiceImpl.java`

Implementación de la lógica de negocio para autenticación.

---

## Configuración de Seguridad

### SecurityConfig
**Ubicación:** `configuration/security/SecurityConfig.java`

```java
@Configuration
@EnableMethodSecurity(prePostEnabled = true)  // Habilita @PreAuthorize
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .csrf(AbstractHttpConfigurer::disable)     // Deshabilitado (usamos JWT)
            .cors(cors -> cors.configure(http))        // CORS configurado
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_URLS).permitAll()  // Rutas públicas
                .anyRequest().authenticated()               // El resto requiere auth
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
```

### ¿Por qué deshabilitamos CSRF?
**CSRF (Cross-Site Request Forgery)** es un ataque donde un sitio malicioso hace que el navegador del usuario envíe una petición no deseada.

Spring Security lo protege con tokens CSRF, pero **no es necesario** cuando:
- Usamos autenticación basada en tokens (JWT)
- El token está en cookies `HttpOnly` + `SameSite`
- La API es stateless (sin sesiones de servidor)

### Configuración CORS
**CORS (Cross-Origin Resource Sharing)** permite que el frontend (en un dominio) acceda al backend (en otro dominio).

```java
@Configuration
public class CorsConfig {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:5173")  // Frontend permitido
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        .allowCredentials(true);  // IMPORTANTE: Permite enviar cookies
}
```

---

## Endpoints de Autenticación

### POST `/api/auth/login`
Inicia sesión y establece la cookie de autenticación.

**Request:**
```json
{
  "correo": "usuario@ejemplo.com",
  "clave": "contraseña123"
}
```

**Response (200 OK):**
```json
{
  "token": null,
  "rol": "PASAJERO"
}
```
*Nota: El token se envía en la cookie `accessToken`, no en el cuerpo.*

**Headers de respuesta:**
```
Set-Cookie: accessToken=<jwt>; Path=/; HttpOnly; SameSite=Lax; Max-Age=36000
```

### POST `/api/auth/logout`
Cierra la sesión eliminando la cookie.

**Response (204 No Content)**

### POST `/api/auth/recuperar-clave`
Solicita recuperación de contraseña.

**Request:**
```json
{
  "correo": "usuario@ejemplo.com"
}
```

### POST `/api/auth/cambiar-clave`
Cambia la contraseña usando un token de recuperación.

**Request:**
```json
{
  "token": "token-de-recuperacion",
  "nuevaClave": "nuevaContraseña123"
}
```

### GET `/api/auth/usuario/me`
Obtiene los datos del usuario autenticado.

**Requiere:** Token válido en cookie  
**Autorización:** `@PreAuthorize("isAuthenticated()")`

---

## Manejo de Errores

### Errores de Token

| Código | Descripción |
|--------|-------------|
| `TOKEN_EXPIRADO` | El token JWT ha expirado |
| `TOKEN_INVALIDO` | El token es inválido o malformado |
| `AUTH_CREDENCIALES_INVALIDAS` | Correo o contraseña incorrectos |
| `AUTH_USUARIO_INACTIVO` | La cuenta está desactivada |

### Respuesta de Error (401 Unauthorized)
```json
{
  "code": "TOKEN_EXPIRADO",
  "message": "El token ha expirado"
}
```

---

## Buenas Prácticas Implementadas

1. **Contraseñas hasheadas**: Se usa BCrypt para almacenar contraseñas
2. **Tokens con expiración**: JWT expira en 10 horas
3. **HttpOnly cookies**: El token no es accesible desde JavaScript
4. **SameSite**: Protección contra CSRF
5. **Validación en cada petición**: El filtro valida el token en cada request
6. **Roles centralizados**: Autorización basada en roles del sistema

---

## Diagrama de Clases

```
┌─────────────────┐       ┌─────────────────┐
│  AuthController │──────▶│   IAuthService  │
└─────────────────┘       └────────┬────────┘
                                   │
                          ┌────────▼────────┐
                          │ AuthServiceImpl │
                          └────────┬────────┘
                                   │
          ┌────────────────────────┼────────────────────────┐
          │                        │                        │
          ▼                        ▼                        ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│UsuarioRepository│     │   JwtServices   │     │PasswordEncoder │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

---

## Referencias

- [JWT.io](https://jwt.io/) - Documentación oficial de JWT
- [Spring Security](https://spring.io/projects/spring-security) - Documentación de Spring Security
- [OWASP Authentication](https://owasp.org/www-project-cheat-sheets/cheatsheets/Authentication_Cheat_Sheet) - Mejores prácticas de autenticación

