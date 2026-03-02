package com.sena.BogotaMetroApp.security;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import com.sena.BogotaMetroApp.utils.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("transaccionSecurityService")
@RequiredArgsConstructor
public class TransaccionSecurityService {

    private final UsuarioRepository usuarioRepository;

    // Valida si el usuario logueado es el dueño del ID o si es Soporte
    public boolean esDuenioOEsSoporte(Long idUsuarioSolicitado) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!estaAutenticado(auth)) return false;

        if (!esSoporte(auth)) return true;

        Long idAutenticado = obtenerIdUsuarioAutenticado(auth);

        return idAutenticado.equals(idUsuarioSolicitado);
    }

    private boolean estaAutenticado(Authentication auth) {
        return auth != null && auth.isAuthenticated();
    }

    private boolean esSoporte(Authentication auth) {
        return auth.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(RoleEnum.SOPORTE.name()));
    }

    // Método auxiliar para extraer el ID de forma segura
    public Long obtenerIdUsuarioAutenticado(Authentication auth) {
        Object principal = auth.getPrincipal();
        if (principal instanceof Usuario usuario) {
            return  usuario.getId();
        }
        // Fallback si el principal es solo el correo (String)
        return usuarioRepository.findByCorreo(auth.getName())
                .orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_NOT_FOUND))
                .getId();
    }

}