package com.sena.BogotaMetroApp.presentation.controller.authentication;


import com.sena.BogotaMetroApp.presentation.dto.auth.CambioClaveDTO;
import com.sena.BogotaMetroApp.presentation.dto.login.AuthResponse;
import com.sena.BogotaMetroApp.presentation.dto.login.LoginRequest;
import com.sena.BogotaMetroApp.services.auth.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
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



}
