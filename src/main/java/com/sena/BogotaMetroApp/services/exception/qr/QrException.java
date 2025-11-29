package com.sena.BogotaMetroApp.services.exception.qr;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;


public class QrException extends RuntimeException {
    private final ErrorCodeEnum errorCode;

    public QrException(ErrorCodeEnum errorCode) {

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
