package com.sena.BogotaMetroApp.services.exception.auth;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private final String code;
    private final String description;

    public AuthException(ErrorCodeEnum errorCode) {
        super(errorCode.description());
        this.code = errorCode.getCode();
        this.description = errorCode.description();
    }

    public AuthException(ErrorCodeEnum errorCode, String customMessage) {
        super(customMessage);
        this.code = errorCode.getCode();
        this.description = customMessage;
    }
}