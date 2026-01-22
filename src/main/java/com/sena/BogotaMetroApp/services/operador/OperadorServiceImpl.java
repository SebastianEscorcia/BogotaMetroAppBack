package com.sena.BogotaMetroApp.services.operador;


import com.sena.BogotaMetroApp.presentation.dto.operador.OperadorResponseDTO;
import com.sena.BogotaMetroApp.mapper.operador.OperadorMapper;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.operador.Operador;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.persistence.repository.operador.OperadorRepository;
import com.sena.BogotaMetroApp.presentation.dto.operador.RegistroOperadorDTO;

import com.sena.BogotaMetroApp.services.factory.DatosPersonalesFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperadorServiceImpl implements IOperadorService {

    private final OperadorRepository operadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final DatosPersonalesFactory datosPersonalesFactory;
    private final OperadorMapper mapper;


    @Override
    @Transactional
    public OperadorResponseDTO registrar(RegistroOperadorDTO dto) {
        Operador operador = mapper.toEntity(dto);


        usuarioRepository.save(operador.getUsuario());
        operadorRepository.save(operador);

        return mapper.toDTO(operador);
    }

    @Override
    public List<OperadorResponseDTO> listarOperadores(String busqueda) {
        return operadorRepository.buscarPorFiltro(busqueda).stream().map(mapper::toDTO).collect(Collectors.toList());

    }

    @Override
    public OperadorResponseDTO actualizar(Long id, RegistroOperadorDTO dto) {
        Operador operador = operadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operador no encontrado"));

        datosPersonalesFactory.actualizarDatos(operador.getUsuario(), dto);

        return mapper.toDTO(operadorRepository.save(operador));
    }

    @Override
    public void eliminar(Long id) {
        Operador operador = operadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operador no encontrado"));
        Usuario usuario = operador.getUsuario();
        if (!usuario.isEnabled()) {
            throw new RuntimeException("El operador ya está deshabilitado");
        }
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public OperadorResponseDTO obtenerPorId(Long id) {
        Operador op = operadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operador no encontrado"));
        return mapper.toDTO(op);
    }

    @Override
    public OperadorResponseDTO obtenerPorCorreo(String correo) {
        Operador operador = operadorRepository.findByUsuarioCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Operador no encontrado con el correo: " + correo));
        if(!operador.getUsuario().isActivo()){
            throw new RuntimeException("El operador está inactivo");
        }
        return mapper.toDTO(operador);
    }

    @Override
    public void reactivar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }
}
