package com.sena.BogotaMetroApp.services.qr;


import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;

public interface IQrService {

    QrResponseDTO generarQrAcceso(String email);

    // Busca un QR por su contenido, valida expiración y consumo y devuelve la entidad para uso interno
    Qr validarYObtenerPorContenido(String contenidoQr);

    Qr consumirQr(Qr qr);
}
