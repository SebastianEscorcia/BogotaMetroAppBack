package com.sena.BogotaMetroApp.services.exception.rol;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;

public class RolException extends RuntimeException {
    private final ErrorCodeEnum errorCode;

    public RolException( ErrorCodeEnum errorCode) {
        super(errorCode.description());
        this.errorCode = errorCode;
    }

    public String getCode() {
        return this.errorCode.getCode();
    }

    public String getDescription() {
        return this.errorCode.description();
    }
}
