package com.sena.BogotaMetroApp.services.auth;

import com.sena.BogotaMetroApp.persistence.models.PasswordResetToken;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.PasswordResetTokenRepository;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.presentation.dto.login.AuthResponse;
import com.sena.BogotaMetroApp.presentation.dto.login.LoginRequest;
import com.sena.BogotaMetroApp.services.EmailService;
import com.sena.BogotaMetroApp.services.JwtServices;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JwtServices jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        if (!usuario.isEnabled()) {
            throw new BadCredentialsException("El usuario está inactivo");
        }

        if (!passwordEncoder.matches(request.getClave(), usuario.getClave())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(usuario);


        return new AuthResponse(token, usuario.getRol().getNombre());
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    @Transactional
    public void solicitarRecuperacion(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Si el correo existe, se enviará un enlace."));
        tokenRepository.deleteByUsuario(usuario);
        PasswordResetToken token = new PasswordResetToken(usuario);
        tokenRepository.save(token);
        emailService.enviarEmailRecuperacion(usuario.getCorreo(), token.getToken());
    }

    @Override
    @Transactional
    public void cambiarClave(String token, String nuevaClave) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido o no encontrado"));

        if (resetToken.estaExpirado()) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("El enlace de recuperación ha expirado");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setClave(passwordEncoder.encode(nuevaClave));
        usuarioRepository.save(usuario);


        tokenRepository.delete(resetToken);
    }
}
