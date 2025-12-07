package com.sena.BogotaMetroApp.errors;

public enum ErrorCodeEnum implements ErrorCode {
    //QR Errors
    QR_NOT_FOUND("QR_NOT_FOUND", "QR no válido o no encontrado en el sistema"),
    QR_ALREADY_USED("QR_ALREADY_USED", "Este QR ya fue utilizado anteriormente"),
    QR_EXPIRED("QR_EXPIRED", "QR expirado. Por favor genere uno nuevo"),
    QR_INVALID_TYPE("QR_INVALID_TYPE", "Este QR no es válido para ingreso al metro"),
    QR_NO_PREVIOUS("QR_NO_PREVIOUS", "No existe un QR previo asociado a este pago"),
    QR_STILL_VALID("QR_STILL_VALID", "El QR actual aún es válido. No requiere regeneración"),
    QR_NO_VIAJE("QR_NO_VIAJE", "El QR anterior no tiene un viaje asociado"),

    QR_PAGO_REQUIRED("QR_PAGO_REQUIRED", "Para QR tipo PAGO debe proporcionar un ID de pago válido"),
    QR_VIAJE_REQUIRED("QR_VIAJE_REQUIRED", "Para QR tipo VIAJE debe proporcionar un ID de viaje válido"),
    QR_INVALID_COMBINATION("QR_INVALID_COMBINATION", "Un QR no puede tener simultáneamente pago y viaje asociados"),

    QR_OK("QR_OK", "¡Bienvenido a MetroBogotá! Pase permitido"),

    // Pago Errors
    PAGO_NOT_FOUND("PAGO_NOT_FOUND", "Pago no encontrado en el sistema"),
    PAGO_INVALID("PAGO_INVALID", "Pago inválido"),
    PAGO_DUPLICADO("PAGO_DUPLICADO", "Ya existe un pago con esta referencia"),
    SALDO_INSUFICIENTE("SALDO_INSUFICIENTE" , "Saldo Insuficiente"),

    // Usuario Errors
    USUARIO_NOT_FOUND("USUARIO_NOT_FOUND", "Usuario no encontrado en  el sistema"),
    USUARIO_INACTIVO("USUARIO_INACTIVO", "El usuario está inactivo"),
    USUARIO_DONT_CARD_ACTIVE("USUARIO_SIN_TARJETA" , "El usuario no tiene una tarjeta virtual activa"),
    USUARIO_YA_EXISTE("USER-409", "El usuario ya existe"),
    // Pasajero Errors
    PASAJERO_USUARIO_NO_EXISTE("PAS-404", "El usuario no existe"),
    PASAJERO_YA_EXISTE("PAS-409", "El usuario ya está registrado "),
    PASAJERO_NO_ENCONTRADO("PAS-404B", "Pasajero no encontrado"),

    // Viaje Errors
    VIAJE_NOT_FOUND("VIAJE_NOT_FOUND", "Viaje no encontrado en el sistema"),
    VIAJE_YA_FINALIZADO("VIAJE_YA_FINALIZADO", "Este viaje ya ha finalizado"),

    //Pasarela Errors
    PASARELA_NOT_FOUND("PASARELA_NOT_FOUND", "Pasarela de pago no encontrada"),
    PASARELA_INACTIVA("PASARELA_INACTIVA", "La pasarela de pago está inactiva"),

    // Tarjeta Errors,
    NUM_CARD_FORMAT("NUM_CARD_FORMAT", "Número de la tarjeta no tiene formato"),
    //General Errors
    INTERNAL_ERROR("INTERNAL_ERROR", "Error interno del servidor"),
    VALIDATION_ERROR("VALIDATION_ERROR", "Error de validación de datos"),
    UNAUTHORIZED("UNAUTHORIZED", "No tiene autorización para realizar esta acción"),
    FORBIDDEN("FORBIDDEN", "Acceso denegado"),

    // Chats Errors

    CHAT_SESION_NOT_FOUND("CHAT_NOT_FOUND", "La sesión de chat no existe"),
    CHAT_CERRADO("CHAT_CLOSED", "No se pueden enviar mensajes a una sesión cerrada"),
    CHAT_ACCESO_DENEGADO("CHAT_FORBIDDEN", "Acceso denegado: Usted no es un participante activo de este chat"),
    CHAT_REMITENTE_NOT_FOUND("CHAT_USER_NOT_FOUND", "El usuario remitente no existe"),
    CHAT_SOPORTE_INVALIDO("CHAT_SUPPORT_INVALID", "El usuario asignado no tiene rol de SOPORTE");

    private final String code;
    private final String description;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.description = message;
    }

    @Override
    public String description() {
        return description;
    }


    @Override
    public String getCode() {
        return code;
    }
}
