package com.sena.BogotaMetroApp.services.qr;

import com.sena.BogotaMetroApp.persistence.models.qr.Qr;

public interface IQrNoUsadoService {
    void moverQrNoUsadoAndExpirado(Qr qr);
}
