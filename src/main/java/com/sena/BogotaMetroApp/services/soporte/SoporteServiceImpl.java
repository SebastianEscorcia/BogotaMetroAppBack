package com.sena.BogotaMetroApp.services.soporte;


import com.sena.BogotaMetroApp.persistence.models.Role;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.RoleRepository;
import com.sena.BogotaMetroApp.persistence.repository.soporte.SoporteRepository;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteResponseDTO;
import com.sena.BogotaMetroApp.mapper.soporte.SoporteMapper;
import com.sena.BogotaMetroApp.persistence.models.soporte.Soporte;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.presentation.dto.soporte.SoporteUpdateDTO;
import com.sena.BogotaMetroApp.services.factory.DatosPersonalesFactory;
import com.sena.BogotaMetroApp.utils.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SoporteServiceImpl implements ISoporteService {
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final SoporteRepository soporteRepository;
    private final DatosPersonalesFactory datosPersonalesFactory;
    private final SoporteMapper mapper;

    @Override
    public SoporteResponseDTO registrar(SoporteRequestDTO dto) {

        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Role rolSoporte = roleRepository.findByNombre(RoleEnum.SOPORTE.toString())
                .orElseThrow(() -> new RuntimeException("Rol de soporte no encontrado"));

        Soporte soporte = mapper.toEntity(dto, rolSoporte);

        soporteRepository.save(soporte);
        usuarioRepository.save(soporte.getUsuario());

        return mapper.toDTO(soporte);
    }

    @Override
    public List<SoporteResponseDTO> listarSoportes(String busqueda) {
        return soporteRepository.buscarPorFiltro(busqueda).stream().map(mapper::toDTO).collect(Collectors.toList());

    }

    @Override
    public SoporteResponseDTO obtenerPorId(Long id) {
        Soporte soporte = soporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario de Soporte no encontrado"));

        if (!soporte.getUsuario().isEnabled()) {
            throw new RuntimeException("El usuario se encuentra inactivo");
        }

        return mapper.toDTO(soporte);
    }

    @Override
    public SoporteResponseDTO actualizar(Long id, SoporteUpdateDTO dto) {
        Soporte soporte = soporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario de Soporte no encontrado"));

        datosPersonalesFactory.actualizarDatos(soporte.getUsuario(), dto);

        soporteRepository.save(soporte);
        return mapper.toDTO(soporte);
    }

    @Override
    public SoporteResponseDTO obtenerPorCorreo(String correo) {
        Soporte soporte = soporteRepository.findByUsuarioCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario de Soporte no encontrado"));

        if (!soporte.getUsuario().isActivo()) {
            throw new RuntimeException("El usuario se encuentra inactivo");
        }

        return mapper.toDTO(soporte);
    }

    @Override
    public void eliminar(Long id) {
        Soporte soporte = soporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario de Soporte no encontrado"));

        Usuario usuario = soporte.getUsuario();

        if (!usuario.isEnabled()) {
            throw new RuntimeException("El usuario ya se encuentra inactivo");
        }

        // Cambio de estado lógico
        usuario.setActivo(false);

        usuarioRepository.save(usuario);
    }
}
