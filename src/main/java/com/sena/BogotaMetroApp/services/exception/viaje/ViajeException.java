package com.sena.BogotaMetroApp.services.exception.viaje;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;

public class ViajeException extends RuntimeException {
    private final ErrorCodeEnum errorCode;

    public ViajeException(ErrorCodeEnum errorCode) {
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
