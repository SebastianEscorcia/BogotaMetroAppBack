package com.sena.BogotaMetroApp.services.pasajero;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.models.DatosPersonales;
import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroResponseDTO;
import com.sena.BogotaMetroApp.mapper.pasajero.PasajeroMapper;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.persistence.repository.pasajero.PasajeroRepository;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroUpdateDTO;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.RegistroPasajeroUnificadoDTO;
import com.sena.BogotaMetroApp.services.exception.pasajero.PasajeroException;
import com.sena.BogotaMetroApp.services.factory.DatosPersonalesFactory;
import com.sena.BogotaMetroApp.services.factory.PasajeroFactory;
import com.sena.BogotaMetroApp.services.factory.TarjetaVirtualFactory;
import com.sena.BogotaMetroApp.services.factory.UsuarioFactory;
import com.sena.BogotaMetroApp.utils.enums.RoleEnum;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasajeroServiceImpl implements IPasajeroService {
    private final PasajeroRepository pasajeroRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioFactory usuarioFactory;
    private final DatosPersonalesFactory datosPersonalesFactory;
    private final PasajeroFactory pasajeroFactory;
    private final TarjetaVirtualFactory tarjetaVirtualFactory;
    private final PasajeroMapper mapper;

    @Override
    @Transactional
    public PasajeroResponseDTO registrarConUsuario(RegistroPasajeroUnificadoDTO dto) {
        usuarioRepository.findByCorreo(dto.getCorreo())
                .ifPresent(u -> {
                    throw new PasajeroException(ErrorCodeEnum.PASAJERO_YA_EXISTE);
                });
        usuarioRepository.findByDatosPersonales_NumDocumento(dto.getNumDocumento()).ifPresent(u -> {
            throw new PasajeroException(ErrorCodeEnum.PASAJERO_YA_EXISTE);
        });

        //El cascade = CascadeType.ALL hace que cuando guarde al usuario, TODO se guarde automáticamente.
        Usuario usuario = usuarioFactory.crearDesdeRegistro(dto, RoleEnum.PASAJERO.toString());

        DatosPersonales dp = datosPersonalesFactory.crearDesdeRegistro(dto, usuario);
        usuario.setDatosPersonales(dp);


        Pasajero pasajero = pasajeroFactory.crear(usuario);
        usuario.setPasajero(pasajero);


        TarjetaVirtual nuevaTarjeta = tarjetaVirtualFactory.crearTarjetaVirtual(pasajero);
        pasajero.setTarjetaVirtual(nuevaTarjeta);

        usuario = usuarioRepository.save(usuario);

        return mapper.toDTO(usuario.getPasajero());
    }

    @Override
    public PasajeroResponseDTO obtenerPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo: " + correo));

        if (usuario.getPasajero() == null) {
            throw new PasajeroException(ErrorCodeEnum.PASAJERO_NO_ENCONTRADO);
        }

        return mapper.toDTO(usuario.getPasajero());
    }

    @Override
    public PasajeroResponseDTO obtener(Long id) {
        Pasajero p = pasajeroRepository.findById(id)
                .orElseThrow(() ->
                        new PasajeroException(ErrorCodeEnum.PASAJERO_NO_ENCONTRADO)
                );

        return mapper.toDTO(p);
    }

    @Override
    public List<PasajeroResponseDTO> listarTodos() {

        return pasajeroRepository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Pasajero p = pasajeroRepository.findById(id).orElseThrow(() -> new PasajeroException(ErrorCodeEnum.PASAJERO_NO_ENCONTRADO));
        Usuario usuario = p.getUsuario();
        if (usuario != null) {
            usuario.setPasajero(null);
        }
        pasajeroRepository.delete(p);
    }

    @Override
    public PasajeroResponseDTO actualizar(Long id, PasajeroUpdateDTO dto) {

        Pasajero pasajero = pasajeroRepository.findById(id)
                .orElseThrow(() -> new PasajeroException(ErrorCodeEnum.PASAJERO_NO_ENCONTRADO));

        mapper.updateEntity(dto, pasajero);

        pasajeroRepository.save(pasajero);

        return mapper.toDTO(pasajero);
    }


}
