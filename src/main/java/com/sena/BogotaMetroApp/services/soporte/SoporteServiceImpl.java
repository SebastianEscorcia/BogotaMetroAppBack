package com.sena.BogotaMetroApp.services.soporte;


import com.sena.BogotaMetroApp.persistence.models.Role;
import com.sena.BogotaMetroApp.persistence.repository.RoleRepository;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteResponseDTO;
import com.sena.BogotaMetroApp.mapper.soporte.SoporteMapper;
import com.sena.BogotaMetroApp.persistence.models.soporte.Soporte;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SoporteServiceImpl implements ISoporteService {
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final SoporteMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SoporteResponseDTO registrar(SoporteRequestDTO dto) {

        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Role rolSoporte = roleRepository.findByNombre("SOPORTE")
                .orElseThrow(() -> new RuntimeException("Rol de soporte no encontrado"));

        String claveEncriptada = passwordEncoder.encode(dto.getClave());

        Soporte soporte = mapper.toEntity(dto, claveEncriptada, rolSoporte);


        usuarioRepository.save(soporte.getUsuario());


        return mapper.toDTO(soporte);
    }
}
