package com.sena.BogotaMetroApp.services.exception.tarifasistema;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;

public class TarifaSistemaException extends RuntimeException {

    private final ErrorCodeEnum errorCode;

    public TarifaSistemaException(ErrorCodeEnum errorCode) {
        super(errorCode.description());
        this.errorCode = errorCode;
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }

    public String getCode() {
        return errorCode.getCode();
    }

    public String getDescription() {
        return errorCode.description();
    }
}