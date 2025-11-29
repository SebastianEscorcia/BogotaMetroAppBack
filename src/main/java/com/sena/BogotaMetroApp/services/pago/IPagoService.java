package com.sena.BogotaMetroApp.services.pago;

import com.sena.BogotaMetroApp.presentation.dto.pago.PagoRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.pago.PagoResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface IPagoService {
    PagoResponseDTO registrarPago(PagoRequestDTO dto);

    PagoResponseDTO obtenerPagoPorId(Long id);

    List<PagoResponseDTO> obtenerPagosPorUsuario(Long idUsuario);

    List<PagoResponseDTO> obtenerPagosPorPasarela(Long idPasarela);

    PagoResponseDTO obtenerPagoPorReferencia(String referencia);

    List<PagoResponseDTO> obtenerPagosPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<PagoResponseDTO> obtenerPagosPorUsuarioYFechas(Long idUsuario, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
