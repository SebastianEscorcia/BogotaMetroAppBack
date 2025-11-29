package com.sena.BogotaMetroApp.services.exception.usuario;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;

public class UsuarioException extends RuntimeException {
    private final ErrorCodeEnum errorCode;

    public UsuarioException(ErrorCodeEnum errorCode) {
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
