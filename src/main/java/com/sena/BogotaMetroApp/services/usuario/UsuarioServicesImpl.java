package com.sena.BogotaMetroApp.services.usuario;

import com.sena.BogotaMetroApp.presentation.dto.usuario.UsuarioRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.usuario.UsuarioResponseDTO;
import com.sena.BogotaMetroApp.mapper.UsuarioMapper;
import com.sena.BogotaMetroApp.persistence.models.Role;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.RoleRepository;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServicesImpl implements IUsuarioServices {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final UsuarioMapper mapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto) {
        if(usuarioRepository.findByCorreo(dto.getCorreo()).isPresent()){
            throw new RuntimeException("Usuario Ya existe");
        }
        Role rol = roleRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setCorreo(dto.getCorreo());
        usuario.setClave(bCryptPasswordEncoder.encode(dto.getClave()) );
        usuario.setRol(rol);

        Usuario guardado = usuarioRepository.save(usuario);

        return mapper.toDTO(guardado);
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public UsuarioResponseDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapper.toDTO(usuario);
    }
}
