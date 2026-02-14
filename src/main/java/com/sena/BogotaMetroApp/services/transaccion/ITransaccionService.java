package com.sena.BogotaMetroApp.services.transaccion;

import com.sena.BogotaMetroApp.mapper.pago.TransaccionMapper;
import com.sena.BogotaMetroApp.persistence.models.transaccion.Recarga;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.transaccion.TransaccionResponseDTO;
import com.sena.BogotaMetroApp.utils.enums.MedioPagoEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ITransaccionService {
    TransaccionResponseDTO registrarRecarga(TransaccionRequestDTO dto);

    TransaccionResponseDTO obtenerTransaccionPorId(Long id);

    List<TransaccionResponseDTO> obtenerTransaccionesPorUsuario(Long idUsuario);

    List<TransaccionResponseDTO> obtenerTransaccionesPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<TransaccionResponseDTO> obtenerTransaccionesPorUsuarioYFechas(Long idUsuario, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<TransaccionResponseDTO> obtenerTransaccionesAvanzadas(Long idUsuario, LocalDateTime inicio, LocalDateTime fin, BigDecimal min, BigDecimal max);
    List<TransaccionResponseDTO> obtenerTransaccionPorNumDocumentoUsuario(String numDocumento);
    List<TransaccionResponseDTO> obtenerTransaccionPorNombre(String nombre);
    List<Recarga> obtenerRecargasPorMedioPago(MedioPagoEnum medioPago);
}
