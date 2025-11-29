package com.sena.BogotaMetroApp.services.exception.pago;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;

public class PagoException extends RuntimeException {

    private final ErrorCodeEnum errorCode;

    public PagoException(ErrorCodeEnum errorCode) {
        super(errorCode.description());
        this.errorCode = errorCode;

    }

    public String getCode() {
        return errorCode.getCode();
    }

    public String getDescription() {
        return  errorCode.description();
    }


}
