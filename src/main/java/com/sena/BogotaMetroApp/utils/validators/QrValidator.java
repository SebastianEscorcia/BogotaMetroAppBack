package com.sena.BogotaMetroApp.utils.validators;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.services.exception.qr.QrException;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class QrValidator {
    private static final int HORAS_VALIDEZ = 24;


    /**
     * Valida el QR completo antes de consumirlo
     */
    public void validarQrParaTorniquete(Qr qr) {
        if (qr == null) {
            throw new QrException(ErrorCodeEnum.QR_NOT_FOUND);
        }

        if (yaFueUsado(qr)) {
            throw new QrException(ErrorCodeEnum.QR_ALREADY_USED);
        }

        if (estaExpirado(qr)) {
            throw new QrException(ErrorCodeEnum.QR_EXPIRED);
        }

        if (!esTipoViaje(qr)) {
            throw new QrException(ErrorCodeEnum.QR_INVALID_TYPE);
        }
    }

    /**
     * Valida si el QR puede ser regenerado
     */
    public void validarParaRegeneracion(Qr qrAnterior) {

        if (qrAnterior == null) {
            throw new QrException(ErrorCodeEnum.QR_NO_PREVIOUS);
        }

        if (yaFueUsado(qrAnterior)) {
            throw new QrException(ErrorCodeEnum.QR_ALREADY_USED);
        }

        if (!estaExpirado(qrAnterior)) {
            throw new QrException(ErrorCodeEnum.QR_STILL_VALID);
        }

        if (qrAnterior.getViaje() == null) {
            throw new QrException(ErrorCodeEnum.QR_NO_VIAJE);
        }
    }

    /**
     * Verifica si el QR está expirado (más de 24 horas)
     */
    public boolean estaExpirado(Qr qr) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusHours(HORAS_VALIDEZ);
        return qr.getFechaGeneracion().isBefore(fechaLimite);
    }

    /**
     * Verifica si el QR ya fue consumido
     */
    public boolean yaFueUsado(Qr qr) {
        return Boolean.TRUE.equals(qr.getConsumido());
    }

    /**
     * Verifica si el QR es de tipo VIAJE
     */
    public boolean esTipoViaje(Qr qr) {
        return qr.getTipo() == TipoQr.VIAJE;
    }

    /**
     * Verifica si el QR aún es válido (no expirado)
     */
    public boolean esValido(Qr qr) {
        return !estaExpirado(qr) && !yaFueUsado(qr);
    }

}
