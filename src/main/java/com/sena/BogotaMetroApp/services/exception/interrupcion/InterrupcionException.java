package com.sena.BogotaMetroApp.services.exception.interrupcion;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import lombok.Getter;

@Getter
public class InterrupcionException extends RuntimeException {
    private final String code;
    private final String description;

    public InterrupcionException(ErrorCodeEnum errorCode) {
        super(errorCode.description());
        this.code = errorCode.getCode();
        this.description = errorCode.description();
    }
}