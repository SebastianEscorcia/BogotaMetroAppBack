package com.sena.BogotaMetroApp.services.operador;

import com.sena.BogotaMetroApp.persistence.models.DatosPersonales;
import com.sena.BogotaMetroApp.persistence.repository.DatosPersonalesRepository;
import com.sena.BogotaMetroApp.presentation.dto.operador.OperadorResponseDTO;
import com.sena.BogotaMetroApp.mapper.operador.OperadorMapper;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.operador.Operador;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.persistence.repository.operador.OperadorRepository;
import com.sena.BogotaMetroApp.presentation.dto.operador.RegistroOperadorDTO;
import com.sena.BogotaMetroApp.services.factory.DatosPersonalesFactory;
import com.sena.BogotaMetroApp.services.factory.UsuarioFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperadorServiceImpl implements IOperadorService {

    private final OperadorRepository operadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final DatosPersonalesRepository datosPersonalesRepository;
    private final UsuarioFactory usuarioFactory;
    private final DatosPersonalesFactory datosPersonalesFactory;
    private final OperadorMapper mapper;


    @Override
    @Transactional
    public OperadorResponseDTO registrar(RegistroOperadorDTO dto) {


        if (usuarioRepository.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya se encuentra registrado en el sistema");
        }

        Usuario usuario = usuarioFactory.crearDesdeRegistro(dto, "OPERADOR");
        usuario = usuarioRepository.save(usuario);


        DatosPersonales dp = datosPersonalesFactory.crearDesdeRegistro(dto, usuario);
        datosPersonalesRepository.save(dp);
        usuario.setDatosPersonales(dp);


        Operador operador = new Operador();
        operador.setId(usuario.getId());
        operador.setUsuario(usuario);

        //operador.setId(usuario.getId()); (MapsId lo hace), Persistable evitará el error.
        // // Al guardar, Hibernate llamará a isNew(), devolverá true, y forzará el INSERT.
        operador = operadorRepository.save(operador);
        usuario.setOperador(operador);
        return mapper.toDTO(operador);
    }
}
