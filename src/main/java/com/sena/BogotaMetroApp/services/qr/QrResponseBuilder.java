package com.sena.BogotaMetroApp.services.qr;

import com.sena.BogotaMetroApp.presentation.dto.qr.ValidarQrResponse;
import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class QrResponseBuilder {

    public ValidarQrResponse buildSuccessResponse(Qr qr) {
        ValidarQrResponse response = new ValidarQrResponse();
        response.setPermitido(true);
        response.setMensaje(ErrorCodeEnum.QR_OK.description());
        response.setCodigoEstado(ErrorCodeEnum.QR_OK.getCode());
        response.setIdQr(qr.getId());
        response.setTipo(qr.getTipo());
        response.setFechaOperacion(LocalDateTime.now());

        if (qr.getViaje() != null) {
            response.setIdViaje(qr.getViaje().getId());
        }

        if (qr.getUsuario() != null && qr.getUsuario().getPasajero() != null) {
            response.setIdPasajero(qr.getUsuario().getPasajero().getId());
        }

        return response;
    }

    public ValidarQrResponse buildErrorResponse(ErrorCodeEnum errorCode) {
        ValidarQrResponse response = new ValidarQrResponse();
        response.setPermitido(false);
        response.setMensaje(errorCode.description());
        response.setCodigoEstado(errorCode.getCode());
        response.setFechaOperacion(LocalDateTime.now());
        return response;
    }
}
