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

        if (auth == null || !auth.isAuthenticated()) return false;


        if (auth.getAuthorities().contains(new SimpleGrantedAuthority(RoleEnum.SOPORTE.toString()))) {
            return true;
        }

        Long idAutenticado = obtenerIdUsuarioAutenticado(auth);

        return idAutenticado.equals(idUsuarioSolicitado);
    }

    // Método auxiliar para extraer el ID de forma segura
    public Long obtenerIdUsuarioAutenticado(Authentication auth) {
        if (auth.getPrincipal() instanceof Usuario) {
            return ((Usuario) auth.getPrincipal()).getId();
        }
        // Fallback si el principal es solo el correo (String)
        return usuarioRepository.findByCorreo(auth.getName())
                .orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_NOT_FOUND))
                .getId();
    }

}