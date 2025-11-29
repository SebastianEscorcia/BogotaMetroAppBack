package com.sena.BogotaMetroApp.services.soporte;

import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteResponseDTO;
import com.sena.BogotaMetroApp.mapper.soporte.SoporteMapper;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.soporte.Soporte;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.persistence.repository.soporte.SoporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SoporteServiceImpl implements ISoporteService {
    private final SoporteRepository soporteRepository;
    private final UsuarioRepository usuarioRepository;
    private final SoporteMapper mapper;

    @Override
    public SoporteResponseDTO registrar(SoporteRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Soporte soporte = new Soporte();
        soporte.setUsuario(usuario);
        soporte.setEstado(dto.getEstado());
        soporte.setFechaCreacion(LocalDateTime.now());
        soporte.setUltimoAcceso(LocalDateTime.now());
        return mapper.toDTO(soporteRepository.save(soporte));
    }
}
