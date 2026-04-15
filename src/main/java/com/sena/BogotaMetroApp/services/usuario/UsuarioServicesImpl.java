package com.sena.BogotaMetroApp.services.usuario;

import com.sena.BogotaMetroApp.presentation.dto.usuario.UsuarioRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.usuario.UsuarioResponseDTO;
import com.sena.BogotaMetroApp.mapper.UsuarioMapper;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.services.factory.UsuarioFactory;
import com.sena.BogotaMetroApp.utils.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServicesImpl implements IUsuarioServices {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper mapper;
    private final UsuarioFactory usuarioFactory;

    @Override
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto) {

        Usuario usuario = usuarioFactory.crearAdminDesdeRegistro(dto, RoleEnum.ADMIN.toString());

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
