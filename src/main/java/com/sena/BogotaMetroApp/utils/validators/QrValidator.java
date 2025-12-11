package com.sena.BogotaMetroApp.utils.validators;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.services.exception.qr.QrException;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class QrValidator {

    /**
     * Valida el QR completo antes de consumirlo
     */
    public void validarQrParaTorniquete(Qr qr) {
        if (qr == null) {
            throw new QrException(ErrorCodeEnum.QR_NOT_FOUND);
        }

        if (qr.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new QrException(ErrorCodeEnum.QR_EXPIRED);
        }

        if (Boolean.TRUE.equals(qr.getConsumido())) {
            throw new QrException(ErrorCodeEnum.QR_ALREADY_USED);
        }
    }

}
