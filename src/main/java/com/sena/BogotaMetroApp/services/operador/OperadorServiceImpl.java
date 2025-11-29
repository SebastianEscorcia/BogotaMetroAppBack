package com.sena.BogotaMetroApp.services.operador;

import com.sena.BogotaMetroApp.presentation.dto.operador.OperadorRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.operador.OperadorResponseDTO;
import com.sena.BogotaMetroApp.mapper.operador.OperadorMapper;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.operador.Operador;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.persistence.repository.operador.OperadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperadorServiceImpl implements IOperadorService {

    private final OperadorRepository operadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final OperadorMapper mapper;


    @Override
    public OperadorResponseDTO registrar(OperadorRequestDTO dto) {
        Usuario user = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Operador operador = new Operador();
        operador.setUsuario(user);

        return mapper.toDTO(operadorRepository.save(operador));
    }
}
