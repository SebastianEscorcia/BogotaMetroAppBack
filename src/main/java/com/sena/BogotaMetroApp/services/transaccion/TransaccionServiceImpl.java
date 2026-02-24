package com.sena.BogotaMetroApp.services.transaccion;


import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.events.pago.PasarSaldoEvent;
import com.sena.BogotaMetroApp.events.pago.RecargaRegistradaEvent;

import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.models.transaccion.PasarSaldo;
import com.sena.BogotaMetroApp.persistence.models.transaccion.Recarga;
import com.sena.BogotaMetroApp.persistence.repository.TarjetaVirtualRepository;
import com.sena.BogotaMetroApp.persistence.repository.transaccion.RecargaRepository;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionResponseDTO;
import com.sena.BogotaMetroApp.mapper.pago.TransaccionMapper;
import com.sena.BogotaMetroApp.persistence.models.transaccion.Transaccion;
import com.sena.BogotaMetroApp.persistence.repository.transaccion.TransaccionRepository;

import com.sena.BogotaMetroApp.services.exception.pago.PagoException;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import com.sena.BogotaMetroApp.services.tarjetavirtual.ItarjetaVirtualService;
import com.sena.BogotaMetroApp.utils.enums.EstadoTarjetaEnum;
import com.sena.BogotaMetroApp.utils.enums.MedioPagoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransaccionServiceImpl implements ITransaccionService {


    private final TransaccionMapper transaccionMapper;
    private final TransaccionRepository transaccionRepository;

    private final RecargaRepository recargaRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TarjetaVirtualRepository tarjetaVirtualRepository;
    private final ItarjetaVirtualService tarjetaVirtualService;

    @Override
    @Transactional
    public TransaccionResponseDTO registrarRecarga(TransaccionRequestDTO dto) {
        Recarga recarga = transaccionMapper.toRecargaEntity(dto);
        Recarga recargaGuardada = transaccionRepository.save(recarga);

        String correoUsuario = recarga.getUsuario().getCorreo();

        eventPublisher.publishEvent(new RecargaRegistradaEvent(
                recargaGuardada.getId(),
                recarga.getUsuario().getId(),
                recarga.getValor(),
                recarga.getMedioDePago(),
                correoUsuario
        ));

        return transaccionMapper.toDTO(recargaGuardada);
    }

    @Override
    public TransaccionResponseDTO obtenerTransaccionPorId(Long id) {
        Transaccion transaccion = transaccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return transaccionMapper.toDTO(transaccion);
    }

    @Override
    public List<TransaccionResponseDTO> obtenerTransaccionesPorUsuario(Long idUsuario) {
        return transaccionRepository.findByUsuarioId(idUsuario)
                .stream()
                .map(transaccionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransaccionResponseDTO> obtenerTransaccionesPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return transaccionRepository.findByFechaBetween(fechaInicio, fechaFin)
                .stream()
                .map(transaccionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransaccionResponseDTO> obtenerTransaccionesPorUsuarioYFechas(Long idUsuario, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return transaccionRepository.findByUsuarioAndFechas(idUsuario, fechaInicio, fechaFin)
                .stream()
                .map(transaccionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransaccionResponseDTO> obtenerTransaccionesAvanzadas(Long idUsuario, LocalDateTime inicio, LocalDateTime fin, BigDecimal min, BigDecimal max) {
        if (inicio == null) inicio = LocalDateTime.of(2000, 1, 1, 0, 0);
        if (fin == null) fin = LocalDateTime.now();
        if (min == null) min = BigDecimal.ZERO;
        if (max == null) max = new BigDecimal("999999999");

        return transaccionRepository.findByFiltrosCombinados(idUsuario, inicio, fin, min, max)
                .stream()
                .map(transaccionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransaccionResponseDTO> obtenerTransaccionPorNumDocumentoUsuario(String numDocumento) {
        return transaccionRepository.findByUsuarioDatosPersonalesNumDocumento(numDocumento).stream().map(transaccionMapper::toDTO).collect(Collectors.toList());
    }


    @Override
    public List<TransaccionResponseDTO> obtenerTransaccionPorNombre(String nombre) {
        return transaccionRepository.findByUsuarioDatosPersonalesNombreCompleto(nombre).stream().map(transaccionMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<Recarga> obtenerRecargasPorMedioPago(MedioPagoEnum medioPago) {
        return recargaRepository.findRecargaByMedioDePago(medioPago);
    }

    @Override
    @Transactional
    public String PasarSaldo(String numTelefono, BigDecimal valor, Long idUsuario) {

        validarRecarga(valor);

        TarjetaVirtual tarjetaOrigen = tarjetaVirtualRepository
                .findByPasajeroUsuarioId(idUsuario)
                .orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_DONT_CARD_ACTIVE));

        validarTarjetaActiva(tarjetaOrigen);

        TarjetaVirtual tarjetaDestino = tarjetaVirtualRepository
                .findTarjetaVirtualByPasajeroUsuarioDatosPersonalesTelefono(numTelefono)
                .orElseThrow(() -> new RuntimeException("No se encontró tarjeta para el número: " + numTelefono));

        validarTarjetaActiva(tarjetaDestino);

        if (tarjetaOrigen.getIdTarjeta().equals(tarjetaDestino.getIdTarjeta())) {
            throw new RuntimeException("No puedes transferirte saldo a ti mismo.");
        }

        tarjetaVirtualService.descontarSaldo(idUsuario, valor);

        tarjetaDestino.setSaldo(tarjetaDestino.getSaldo().add(valor));
        tarjetaVirtualRepository.save(tarjetaDestino);

        PasarSaldo transaccion = crearTransaccionPasarSaldo(tarjetaOrigen, tarjetaDestino, valor, numTelefono);
        transaccion.setMedioDePago(MedioPagoEnum.TRANSFERENCIA_ENVIADA);

        PasarSaldo transaccionGuardada = transaccionRepository.save(transaccion);

        eventPublisher.publishEvent(new PasarSaldoEvent(
                transaccionGuardada.getId(),
                tarjetaDestino.getPasajero().getUsuario().getId(),
                transaccionGuardada.getTarjetaDestino().getSaldo(),
                transaccionGuardada.getMedioDePago(),
                valor,
                tarjetaDestino.getPasajero().getUsuario().getCorreo()
        ));

        return "Transferencia realizada exitosamente.";
    }

    private void validarRecarga(BigDecimal valor) {

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PagoException(ErrorCodeEnum.PAGO_INVALID);
        }
    }

    private void validarTarjetaActiva(TarjetaVirtual tarjeta) {
        if (tarjeta == null) {
            throw new UsuarioException(ErrorCodeEnum.USUARIO_DONT_CARD_ACTIVE);
        }
        if (!EstadoTarjetaEnum.ACTIVA.equals(tarjeta.getEstado())) {
            throw new PagoException(ErrorCodeEnum.USUARIO_DONT_CARD_ACTIVE);
        }
    }

    private PasarSaldo crearTransaccionPasarSaldo(TarjetaVirtual tarjetaOrigen, TarjetaVirtual tarjetaDestino, BigDecimal valor, String numTelefono) {
        PasarSaldo transaccion = new PasarSaldo();
        transaccion.setUsuario(tarjetaOrigen.getPasajero().getUsuario());
        transaccion.setValor(valor);
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setDescripcion("Transferencia a tarjeta: " + numTelefono);
        transaccion.setTarjetaVirtual(tarjetaOrigen);
        transaccion.setTarjetaOrigen(tarjetaOrigen);
        transaccion.setTarjetaDestino(tarjetaDestino);
        return transaccion;
    }
}
