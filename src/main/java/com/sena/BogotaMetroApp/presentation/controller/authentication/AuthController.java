package com.sena.BogotaMetroApp.presentation.controller.authentication;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.presentation.dto.login.AuthResponse;
import com.sena.BogotaMetroApp.presentation.dto.login.LoginRequest;
import com.sena.BogotaMetroApp.services.JwtServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtServices jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!bCryptPasswordEncoder.matches(request.getClave(), usuario.getClave())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(usuario);

        return ResponseEntity.ok(new AuthResponse(token, usuario.getRol().getNombre()));
    }
}
