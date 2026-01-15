package com.sena.BogotaMetroApp.services.exception;


import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.services.exception.auth.AuthException;
import com.sena.BogotaMetroApp.services.exception.chat.ChatException;
import com.sena.BogotaMetroApp.services.exception.horariosistema.HorarioSistemaException;
import com.sena.BogotaMetroApp.services.exception.interrupcion.InterrupcionException;
import com.sena.BogotaMetroApp.services.exception.pago.PagoException;
import com.sena.BogotaMetroApp.services.exception.pasajero.PasajeroException;
import com.sena.BogotaMetroApp.services.exception.qr.QrException;
import com.sena.BogotaMetroApp.services.exception.ruta.RutaException;
import com.sena.BogotaMetroApp.services.exception.tarifasistema.TarifaSistemaException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    /* --- Excepciones de Chat --- */

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<ErrorResponse> handleChatException(ChatException ex) {
        log.warn("Error Chat: {}", ex.getDescription());
        return buildErrorResponse(ex.getCode(), ex.getDescription(), HttpStatus.BAD_REQUEST);
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
    /* --- Excepciones de Integridad de Datos --- */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Error de integridad de datos: {}", ex.getMessage());
        return buildErrorResponse(
                ErrorCodeEnum.INTERNAL_ERROR.getCode(),
                "Error de integridad de datos. Por favor, intente nuevamente.",
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(RutaException.class)
    public ResponseEntity<ErrorResponse> handleRutaException(RutaException ex) {
        log.error("Error Ruta: {}", ex.getDescription());
        HttpStatus status = ex.getCode().contains("NOT_FOUND") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return buildErrorResponse(ex.getCode(), ex.getDescription(), status);
    }

    @ExceptionHandler(InterrupcionException.class)
    public ResponseEntity<ErrorResponse> handleInterrupcionException(InterrupcionException ex) {
        log.error("Error Interrupción: {}", ex.getDescription());
        HttpStatus status = ex.getCode().contains("NOT_FOUND") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return buildErrorResponse(ex.getCode(), ex.getDescription(), status);
    }

    /* --- Excepciones de HorarioSistema --- */
    @ExceptionHandler(HorarioSistemaException.class)
    public ResponseEntity<ErrorResponse> handleHorarioSistemaException(HorarioSistemaException ex) {
        log.error("Error HorarioSistema: {}", ex.getMessage());
        HttpStatus status = ex.getErrorCode().name().contains("NOT_FOUND") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return buildErrorResponse(ex.getErrorCode().getCode(), ex.getErrorCode().description(), status);
    }

    /* --- Excepciones de TarifaSistema --- */
    @ExceptionHandler(TarifaSistemaException.class)
    public ResponseEntity<ErrorResponse> handleTarifaSistemaException(TarifaSistemaException ex) {
        log.error("Error TarifaSistema: {}", ex.getMessage());
        HttpStatus status = ex.getCode().contains("NOT_FOUND") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return buildErrorResponse(ex.getCode(), ex.getDescription(), status);
    }

    /* --- Error de Formato JSON (Enum inválido, sintaxis rota) --- */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonErrors(HttpMessageNotReadableException ex) {
        log.warn("Error de lectura JSON: {}", ex.getMessage());

        String message = ErrorCodeEnum.JSON_FORMAT_ERROR.description();

        if (ex.getMessage().contains("InterruptionTypeEnum")) {
            message = "El tipo de interrupción no es válido. Valores permitidos: MANTENIMIENTO, ACCIDENTE, CLIMA, OTRO";
        }

        return buildErrorResponse(
                ErrorCodeEnum.JSON_FORMAT_ERROR.getCode(),
                message,
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

    /* --- Excepciones de Autenticación --- */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex) {
        log.warn("Error Auth: {}", ex.getDescription());
        HttpStatus status = ex.getCode().contains("NOT_FOUND") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return buildErrorResponse(ex.getCode(), ex.getDescription(), status);
    }



}
