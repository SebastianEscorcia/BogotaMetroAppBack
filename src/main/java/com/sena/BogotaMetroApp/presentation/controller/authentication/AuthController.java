package com.sena.BogotaMetroApp.presentation.controller.authentication;


import com.sena.BogotaMetroApp.presentation.dto.auth.CambioClaveDTO;
import com.sena.BogotaMetroApp.presentation.dto.auth.UserAfterAuthDTO;
import com.sena.BogotaMetroApp.presentation.dto.login.AuthResponse;
import com.sena.BogotaMetroApp.presentation.dto.login.LoginRequest;
import com.sena.BogotaMetroApp.services.auth.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {

        AuthResponse auth = authService.login(request);
        ResponseCookie cookie = ResponseCookie.from("accessToken", auth.getToken())
                .httpOnly(true)
                .secure(false) // True en producción con HTTPS
                .sameSite("Lax") // O "None" + "secure (true)" si frontend y backend están en dominios diferentes
                .path("/")
                .maxAge(Duration.ofHours(10)).build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new AuthResponse(null, auth.getRol())); // No enviamos el token en el cuerpo, solo en la cookie
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie clearCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false) // true en producción
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());
        SecurityContextHolder.clearContext();
        // authService.logout();
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/recuperar-clave")
    public ResponseEntity<Map<String, String>> solicitarRecuperacion(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        authService.solicitarRecuperacion(correo);
        return ResponseEntity.ok(Map.of("mensaje", "Solicitud procesada. Verifique su correo."));
    }

    @PostMapping("/cambiar-clave")
    public ResponseEntity<Map<String, String>> cambiarClave(@RequestBody CambioClaveDTO dto) {
        authService.cambiarClave(dto.getToken(), dto.getNuevaClave());
        return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada exitosamente."));
    }

    @GetMapping("/usuario/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserAfterAuthDTO> obtenerDatosUsuario() {
        return ResponseEntity.ok(authService.obterMisDatos());
    }

}
