package com.sena.BogotaMetroApp.services.pasajero;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.models.DatosPersonales;
import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.repository.DatosPersonalesRepository;
import com.sena.BogotaMetroApp.persistence.repository.TarjetaVirtualRepository;
import com.sena.BogotaMetroApp.presentation.dto.pasajero.PasajeroRequestDTO;
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
import com.sena.BogotaMetroApp.services.factory.UsuarioFactory;
import com.sena.BogotaMetroApp.utils.logic.TarjetaUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasajeroServiceImpl implements IPasajeroService {
    private final PasajeroRepository pasajeroRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioFactory usuarioFactory;
    private final DatosPersonalesRepository datosPersonalesRepository;
    private final DatosPersonalesFactory datosPersonalesFactory;
    private final TarjetaVirtualRepository tarjetaVirtualRepository;
    private final PasajeroFactory pasajeroFactory;
    private final PasajeroMapper mapper;

    @Override
    @Transactional
    public PasajeroResponseDTO registrar(PasajeroRequestDTO dto) {
        Usuario user = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new PasajeroException(ErrorCodeEnum.PASAJERO_USUARIO_NO_EXISTE));


        if (user.getPasajero() != null) {
            throw new PasajeroException(ErrorCodeEnum.PASAJERO_YA_EXISTE);
        }

        Pasajero pasajero = new Pasajero();
        pasajero.setUsuario(user);
        pasajero.setId(user.getId());

        TarjetaVirtual nuevaTarjeta = new TarjetaVirtual();
        nuevaTarjeta.setSaldo(BigDecimal.ZERO);
        nuevaTarjeta.setEstado("ACTIVA");
        nuevaTarjeta.setNumeroTarjeta(TarjetaUtil.generarNumeroTarjeta());
        nuevaTarjeta.setPasajero(pasajero);
        pasajero.setTarjetaVirtual(nuevaTarjeta);

        user.setPasajero(pasajero);
        tarjetaVirtualRepository.save(nuevaTarjeta);
        pasajeroRepository.save(pasajero);
        usuarioRepository.save(user);

        return mapper.toDTO(pasajero);
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

    @Override
    public PasajeroResponseDTO registrarConUsuario(RegistroPasajeroUnificadoDTO dto) {
        usuarioRepository.findByCorreo(dto.getCorreo())
                .ifPresent(u -> {
                    throw new PasajeroException(ErrorCodeEnum.PASAJERO_YA_EXISTE);
                });

        // Crear objetos con factories
        Usuario usuario = usuarioFactory.crearDesdeRegistro(dto);
        usuario = usuarioRepository.save(usuario);

        DatosPersonales dp = datosPersonalesFactory.crearDesdeRegistro(dto, usuario);
        usuario.setDatosPersonales(dp);
        datosPersonalesRepository.save(dp);

        Pasajero pasajero = pasajeroFactory.crear(usuario);
        pasajero.setId(usuario.getId());
        pasajero = pasajeroRepository.save(pasajero);
        usuario.setPasajero(pasajero);


        TarjetaVirtual nuevaTarjeta = new TarjetaVirtual();
        nuevaTarjeta.setSaldo(BigDecimal.ZERO);
        nuevaTarjeta.setEstado("ACTIVA");
        nuevaTarjeta.setNumeroTarjeta(TarjetaUtil.generarNumeroTarjeta());
        nuevaTarjeta.setPasajero(pasajero);


        tarjetaVirtualRepository.save(nuevaTarjeta);
        pasajero.setTarjetaVirtual(nuevaTarjeta);

        // Respuesta
        return mapper.toDTO(pasajero);
    }


}
