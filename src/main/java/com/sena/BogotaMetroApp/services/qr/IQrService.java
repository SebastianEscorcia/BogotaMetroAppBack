package com.sena.BogotaMetroApp.services.qr;

import com.sena.BogotaMetroApp.presentation.dto.qr.QrRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.ValidarQrRequest;
import com.sena.BogotaMetroApp.presentation.dto.qr.ValidarQrResponse;

public interface IQrService {
    QrResponseDTO generarQr(QrRequestDTO request);

    ValidarQrResponse validarQrEnTorniquete(ValidarQrRequest request);

    QrResponseDTO regenerarQrViaje(Long idPago);
}
