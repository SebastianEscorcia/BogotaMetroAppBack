package com.sena.BogotaMetroApp.services.transaccion;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.models.transaccion.Recarga;
import com.sena.BogotaMetroApp.persistence.repository.TarjetaVirtualRepository;
import com.sena.BogotaMetroApp.persistence.repository.transaccion.RecargaRepository;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionResponseDTO;
import com.sena.BogotaMetroApp.mapper.pago.TransaccionMapper;
import com.sena.BogotaMetroApp.persistence.models.transaccion.Transaccion;
import com.sena.BogotaMetroApp.persistence.repository.transaccion.TransaccionRepository;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import lombok.RequiredArgsConstructor;
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
    private final TarjetaVirtualRepository tarjetaVirtualRepository;
    private final RecargaRepository recargaRepository;

    @Override
    @Transactional
    public TransaccionResponseDTO registrarRecarga(TransaccionRequestDTO dto) {
        Recarga recarga = transaccionMapper.toRecargaEntity(dto);

        TarjetaVirtual tarjetaVirtual = tarjetaVirtualRepository.findByPasajeroUsuarioId(recarga.getUsuario().getId()).orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_DONT_CARD_ACTIVE));

        tarjetaVirtual.setSaldo(tarjetaVirtual.getSaldo().add(recarga.getValor()));
        tarjetaVirtualRepository.save(tarjetaVirtual);
        recarga.setTarjetaVirtual(tarjetaVirtual);

        return transaccionMapper.toDTO(transaccionRepository.save(recarga));
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
    public List<TransaccionResponseDTO> obtenerTransaccionesPorPasarela(Long idPasarela) {
        return recargaRepository.findByPasarelaId(idPasarela)
                .stream()
                .map(transaccionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TransaccionResponseDTO obtenerTransaccionPorReferencia(String referencia) {
        Recarga recarga = recargaRepository.findByReferenciaPasarela(referencia)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con esa referencia"));
        return transaccionMapper.toDTO(recarga);
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
}

