package com.sena.BogotaMetroApp.services.exception.ruta;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import lombok.Getter;

@Getter
public class RutaException extends RuntimeException {
    private final String code;
    private final String description;

    public RutaException(ErrorCodeEnum errorCode) {
        super(errorCode.description());
        this.code = errorCode.getCode();
        this.description = errorCode.description();
    }
}