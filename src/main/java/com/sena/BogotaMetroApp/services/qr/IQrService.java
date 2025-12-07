package com.sena.BogotaMetroApp.services.qr;

import com.sena.BogotaMetroApp.persistence.models.Viaje;
import com.sena.BogotaMetroApp.persistence.models.transaccion.Transaccion;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.ValidarQrRequest;
import com.sena.BogotaMetroApp.presentation.dto.qr.ValidarQrResponse;

public interface IQrService {
    QrResponseDTO generarQr(QrRequestDTO request);

    Qr generarEntidadQrParaViaje(Pasajero pasajero, Viaje viaje, Transaccion transaccion);

    ValidarQrResponse validarQrEnTorniquete(ValidarQrRequest request);

    QrResponseDTO regenerarQrViaje(Long idPago);
}
