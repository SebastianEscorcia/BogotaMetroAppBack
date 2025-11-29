package com.sena.BogotaMetroApp.services.exception;


import com.sena.BogotaMetroApp.services.exception.pago.PagoException;
import com.sena.BogotaMetroApp.services.exception.pasajero.PasajeroException;
import com.sena.BogotaMetroApp.services.exception.qr.QrException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(QrException.class)
    public ResponseEntity<ErrorResponse> handleQrException(QrException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(ex.getCode())
                .message(ex.getDescription())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PagoException.class)
    public ResponseEntity<ErrorResponse> handlePagoException(PagoException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(ex.getCode())
                .message(ex.getDescription())
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code("INTERNAL_ERROR")
                .message("Error interno del servidor")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(PasajeroException.class)
    public ResponseEntity<ErrorResponse> handlePasajeroException(PasajeroException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(ex.getCode())
                .message(ex.getDescription())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
