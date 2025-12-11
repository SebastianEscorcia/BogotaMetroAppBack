package com.sena.BogotaMetroApp.services.exception.horariosistema;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;

public class HorarioSistemaException extends RuntimeException {
    private final ErrorCodeEnum errorCode;

    public HorarioSistemaException(ErrorCodeEnum errorCode) {
        super(errorCode.description());
        this.errorCode = errorCode;
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }
}