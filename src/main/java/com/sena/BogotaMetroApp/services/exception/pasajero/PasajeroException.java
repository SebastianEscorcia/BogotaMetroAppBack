package com.sena.BogotaMetroApp.services.exception.pasajero;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;

public class PasajeroException extends RuntimeException {

    private final ErrorCodeEnum errorCode;

    public PasajeroException(ErrorCodeEnum errorCode) {
        super(errorCode.description());
        this.errorCode = errorCode;
    }

    public String getCode() {
        return errorCode.getCode();
    }

    public String getDescription() {
        return errorCode.description();
    }
}
