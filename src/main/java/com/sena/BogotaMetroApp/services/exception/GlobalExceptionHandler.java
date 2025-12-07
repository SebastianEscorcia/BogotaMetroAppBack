package com.sena.BogotaMetroApp.services.exception;


import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.services.exception.pago.PagoException;
import com.sena.BogotaMetroApp.services.exception.pasajero.PasajeroException;
import com.sena.BogotaMetroApp.services.exception.qr.QrException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /* --- Excepciones de QR --- */
    @ExceptionHandler(QrException.class)
    public ResponseEntity<ErrorResponse> handleQrException(QrException ex) {
        log.error("Error QR: {}", ex.getMessage());
        return buildErrorResponse(ex.getCode(), ex.getDescription(), HttpStatus.BAD_REQUEST);
    }

    /* --- Excepciones de Pago --- */
    @ExceptionHandler(PagoException.class)
    public ResponseEntity<ErrorResponse> handlePagoException(PagoException ex) {
        log.error("Error Pago: {}", ex.getMessage());
        return buildErrorResponse(ex.getCode(), ex.getDescription(), HttpStatus.BAD_REQUEST);
    }

    /* --- Excepciones de Pasajero --- */
    @ExceptionHandler(PasajeroException.class)
    public ResponseEntity<ErrorResponse> handlePasajeroException(PasajeroException ex) {
        log.error("Error Pasajero: {}", ex.getMessage());
        return buildErrorResponse(ex.getCode(), ex.getDescription(), HttpStatus.NOT_FOUND);
    }

    /* --- Validación con @Valid en DTOs --- */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Error de validación");

        log.warn("Validación fallida: {}", message);

        return buildErrorResponse(
                ErrorCodeEnum.VALIDATION_ERROR.getCode(),
                message,
                HttpStatus.BAD_REQUEST
        );
    }

    /* --- Validación en parámetros (path variables, request params) --- */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("Datos inválidos");

        log.warn("ConstraintViolation: {}", msg);

        return buildErrorResponse(
                ErrorCodeEnum.VALIDATION_ERROR.getCode(),
                msg,
                HttpStatus.BAD_REQUEST
        );
    }

    /* --- Cualquier error inesperado --- */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Error interno no controlado", ex);
        return buildErrorResponse(
                ErrorCodeEnum.INTERNAL_ERROR.getCode(),
                ErrorCodeEnum.INTERNAL_ERROR.description(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /* --- Método utilitario común --- */
    private ResponseEntity<ErrorResponse> buildErrorResponse(String code, String message, HttpStatus status) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(code)
                .message(message)
                .status(status.value())
                .build();

        return new ResponseEntity<>(error, status);
    }

}
