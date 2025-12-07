package com.sena.BogotaMetroApp.services.transaccion;

import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ITransaccionService {
    TransaccionResponseDTO registrarRecarga(TransaccionRequestDTO dto);

    TransaccionResponseDTO obtenerTransaccionPorId(Long id);

    List<TransaccionResponseDTO> obtenerTransaccionesPorUsuario(Long idUsuario);

    List<TransaccionResponseDTO> obtenerTransaccionesPorPasarela(Long idPasarela);

    TransaccionResponseDTO obtenerTransaccionPorReferencia(String referencia);

    List<TransaccionResponseDTO> obtenerTransaccionesPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<TransaccionResponseDTO> obtenerTransaccionesPorUsuarioYFechas(Long idUsuario, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
