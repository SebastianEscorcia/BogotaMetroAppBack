package com.sena.BogotaMetroApp.services.pago;

import com.sena.BogotaMetroApp.presentation.dto.pago.PagoRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pago.PagoResponseDTO;
import com.sena.BogotaMetroApp.mapper.pago.PagoMapper;
import com.sena.BogotaMetroApp.persistence.models.pago.Pago;
import com.sena.BogotaMetroApp.persistence.repository.pago.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements IPagoService {

    private final PagoMapper pagoMapper;
    private final PagoRepository pagoRepository;

    @Override
    @Transactional
    public PagoResponseDTO registrarPago(PagoRequestDTO dto) {
        return pagoMapper.toEntity(dto);
    }

    @Override
    public PagoResponseDTO obtenerPagoPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return pagoMapper.toDTO(pago);
    }

    @Override
    public List<PagoResponseDTO> obtenerPagosPorUsuario(Long idUsuario) {
        return pagoRepository.findByUsuarioId(idUsuario)
                .stream()
                .map(pagoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PagoResponseDTO> obtenerPagosPorPasarela(Long idPasarela) {
        return pagoRepository.findByPasarelaId(idPasarela)
                .stream()
                .map(pagoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PagoResponseDTO obtenerPagoPorReferencia(String referencia) {
        Pago pago = pagoRepository.findByReferenciaPasarela(referencia)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con esa referencia"));
        return pagoMapper.toDTO(pago);
    }

    @Override
    public List<PagoResponseDTO> obtenerPagosPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return pagoRepository.findByFechaPagoBetween(fechaInicio, fechaFin)
                .stream()
                .map(pagoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PagoResponseDTO> obtenerPagosPorUsuarioYFechas(Long idUsuario, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return pagoRepository.findByUsuarioAndFechas(idUsuario, fechaInicio, fechaFin)
                .stream()
                .map(pagoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
