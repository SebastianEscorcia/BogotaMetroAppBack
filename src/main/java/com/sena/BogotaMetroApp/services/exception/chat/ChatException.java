package com.sena.BogotaMetroApp.services.exception.chat;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;

public class ChatException extends RuntimeException {

    private final ErrorCodeEnum errorCode;

    public ChatException(ErrorCodeEnum errorCode) {
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
