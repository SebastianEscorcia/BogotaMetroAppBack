package com.sena.BogotaMetroApp.services.ticket;

import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;

public interface IticketService {
    QrResponseDTO comprarViaje(Long idUsuario, Long idViaje);
}
