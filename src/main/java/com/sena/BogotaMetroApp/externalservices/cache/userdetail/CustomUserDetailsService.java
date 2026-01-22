package com.sena.BogotaMetroApp.externalservices.cache.userdetail;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final CacheManager cacheManager;
    @Override
    @Cacheable(value="userDetails",key="#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading UserDetails desde DB: {}",username);
        Usuario usuario = usuarioRepository.findByCorreo(username).orElseThrow(()->new UsernameNotFoundException("Usuario no encontrado"));
        return new User(usuario.getCorreo(), usuario.getClave(), List.of(new SimpleGrantedAuthority(usuario.getRol().getNombre())));
    }
}
