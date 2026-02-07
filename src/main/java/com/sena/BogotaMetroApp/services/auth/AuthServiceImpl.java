package com.sena.BogotaMetroApp.services.auth;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.mapper.auth.UserAuthMapper;
import com.sena.BogotaMetroApp.persistence.models.PasswordResetToken;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.PasswordResetTokenRepository;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.presentation.dto.auth.UserAfterAuthDTO;
import com.sena.BogotaMetroApp.presentation.dto.login.AuthResponse;
import com.sena.BogotaMetroApp.presentation.dto.login.LoginRequest;
import com.sena.BogotaMetroApp.externalservices.email.TurboSMTPEmailService;
import com.sena.BogotaMetroApp.services.JwtServices;
import com.sena.BogotaMetroApp.services.exception.auth.AuthException;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public  class AuthServiceImpl implements IAuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JwtServices jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TurboSMTPEmailService emailService;
    private final UserAuthMapper userAuthMapper;

    @Override
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new AuthException(ErrorCodeEnum.AUTH_CREDENCIALES_INVALIDAS));

        if (!usuario.isEnabled()) {
            throw new AuthException(ErrorCodeEnum.AUTH_USUARIO_INACTIVO);
        }

        if (!passwordEncoder.matches(request.getClave(), usuario.getClave())) {
            throw new AuthException(ErrorCodeEnum.AUTH_CREDENCIALES_INVALIDAS);
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
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            log.info("Solicitud de recuperación de contraseña para correo no registrado: {}", correo);
            return;
        }
        Usuario usuario = usuarioOpt.get();
        tokenRepository.deleteByUsuario(usuario);
        tokenRepository.flush();

        PasswordResetToken token = new PasswordResetToken(usuario);
        tokenRepository.save(token);

        emailService.enviarEmailRecuperacion(usuario.getCorreo(), token.getToken());
    }

    @Override
    @Transactional
    public void cambiarClave(String token, String nuevaClave) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException(ErrorCodeEnum.AUTH_TOKEN_INVALID));


        if (resetToken.estaExpirado()) {
            tokenRepository.delete(resetToken);
            throw new AuthException(ErrorCodeEnum.AUTH_TOKEN_EXPIRED);
        }


        Usuario usuario = resetToken.getUsuario();
        usuario.setClave(passwordEncoder.encode(nuevaClave));
        usuarioRepository.save(usuario);


        tokenRepository.delete(resetToken);
    }

    @Override
    public UserAfterAuthDTO obterMisDatos() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_NOT_FOUND));

        return userAuthMapper.toDTO(usuario);
    }
}
