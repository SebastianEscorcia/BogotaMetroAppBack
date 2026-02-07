package com.sena.BogotaMetroApp.errors;

public enum ErrorCodeEnum implements ErrorCode {

    //ROL Erros
    ROL_NOT_FOUND("ROL_NOT_FOUND", "Rol no encontrado"),
    ROL_DEACTIVATED("ROL_DEACTIVATED","El rol está desactivado"),
    ROL_YA_EXISTE("ROL_YA_EXISTE","El rol ya está registrado en el sistema"),
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
    SALDO_INSUFICIENTE("SALDO_INSUFICIENTE", "Saldo Insuficiente"),

    // Usuario Errors
    USUARIO_NOT_FOUND("USUARIO_NOT_FOUND", "Usuario no encontrado en  el sistema"),
    USUARIO_INACTIVO("USUARIO_INACTIVO", "El usuario está inactivo"),
    USUARIO_DONT_CARD_ACTIVE("USUARIO_SIN_TARJETA", "El usuario no tiene una tarjeta virtual activa"),
    USUARIO_YA_EXISTE("USER-409", "El usuario ya existe"),
    // Pasajero Errors
    PASAJERO_USUARIO_NO_EXISTE("PAS-404", "El usuario no existe"),
    PASAJERO_YA_EXISTE("PAS-409", "El usuario ya está registrado "),
    PASAJERO_NO_ENCONTRADO("PAS-404B", "Pasajero no encontrado"),
    PASAJERO_INACTIVO("PAS-403", "El pasajero está inactivo"),
    //Pasarela Errors
    PASARELA_NOT_FOUND("PASARELA_NOT_FOUND", "Pasarela de pago no encontrada"),
    PASARELA_INACTIVA("PASARELA_INACTIVA", "La pasarela de pago está inactiva"),

    // Ruta Errors
    RUTA_NOT_FOUND("RUTA_NOT_FOUND", "La ruta solicitada no existe"),
    RUTA_ESTACION_REQ("RUTA_ESTACION_REQ", "Se requieren las estaciones de inicio y fin"),
    RUTA_ESTACION_NOT_FOUND("RUTA_ESTACION_NOT_FOUND", "Una de las estaciones especificadas no existe"),
    RUTA_HORA_FORMATO_INVALIDO("RUTA_HORA_FORMATO_INVALIDO", "El formato de la hora de inicio de la ruta es inválido"),

    // Interrupcion Errors
    INTERRUPCION_NOT_FOUND("INTERRUPCION_NOT_FOUND", "La interrupción solicitada no existe"),
    INTERRUPCION_ESTACION_LINEA_REQ("INTERRUPCION_REQ", "Debe especificar al menos una Estación o una Línea"),
    INTERRUPCION_YA_ELIMINADA("INTERRUPCION_DELETED", "No se puede modificar una interrupción que ya fue eliminada"),
    INTERRUPCION_TIPO_INVALIDO("INTERRUPCION_TYPE_INVALID", "El tipo de interrupción no es válido"),

    // Linea Errors
    LINEA_NOT_FOUND("LINEA_NOT_FOUND", "La línea solicitada no existe"),

    // JSON Errors
    JSON_FORMAT_ERROR("JSON_FORMAT_ERROR", "El formato del JSON enviado es incorrecto o contiene valores no permitidos"),

    // Tarjeta Errors,
    NUM_CARD_FORMAT("NUM_CARD_FORMAT", "Número de la tarjeta no tiene formato"),
    //General Errors
    INTERNAL_ERROR("INTERNAL_ERROR", "Error interno del servidor"),
    VALIDATION_ERROR("VALIDATION_ERROR", "Error de validación de datos"),
    UNAUTHORIZED("UNAUTHORIZED", "No tiene autorización para realizar esta acción"),
    FORBIDDEN("FORBIDDEN", "Acceso denegado"),


    // Transaccion Errors
    ACCESO_DENEGADO("TRANSACCION_FORBIDDEN", "Acceso denegado: Usted no es el propietario de esta transacción"),

    // Horario Sistema Errors
    HORARIO_NOT_FOUND("HORARIO_NOT_FOUND","Horario del sistema no encontrado"),
    HORARIO_INVALID_TIME("HORARIO_INVALID_TIME","Hora de fin debe ser posterior a hora de inicio"),

    // Tarifa Sistema Errors
    TARIFA_NOT_FOUND("TARIFA_NOT_FOUND", "No hay tarifa configurada en el sistema"),
    TARIFA_VALOR_INVALIDO("TARIFA_VALOR_INVALIDO", "El valor de la tarifa debe ser mayor a cero"),
    TARIFA_YA_EXISTE("TARIFA_YA_EXISTE", "Ya existe una tarifa activa en el sistema"),
    // Chats Errors
    CHAT_SESION_NOT_FOUND("CHAT_NOT_FOUND", "La sesión de chat no existe"),
    CHAT_CERRADO("CHAT_CLOSED", "No se pueden enviar mensajes a una sesión cerrada"),
    CHAT_ACCESO_DENEGADO("CHAT_FORBIDDEN", "Acceso denegado: Usted no es un participante activo de este chat"),
    CHAT_REMITENTE_NOT_FOUND("CHAT_USER_NOT_FOUND", "El usuario remitente no existe"),
    CHAT_SOPORTE_INVALIDO("CHAT_SUPPORT_INVALID", "El usuario asignado no tiene rol de SOPORTE"),
    CHAT_PENDIENTE_ASIGNACION("CHAT_PENDING_ASSIGNMENT", "La sesión de chat aún no tiene un soporte asignado"),
    CHAT_REMITENTE_SOPORTE_INACTIVO("CHAT_USER_INACTIVE", "El soporte remitente está inactivo"),
    CHAT_SOPORTE_NOT_FOUND("CHAT_SUPPORT_NOT_FOUND", "No se encontró un usuario con rol de soporte disponible"),
    CHAT_TOMADO_POR_SOPORTE("CHAT_TOMADO_POR_SOPORTE", "Esta sesión ya ha sido tomada por otro agente de soporte"),
    CHAT_MENSAJE_VACIO("CHAT_EMPTY_MESSAGE", "El mensaje no puede estar vacío"),

    // Autenticación y Recuperación Errors
    AUTH_TOKEN_INVALID("AUTH_TOKEN_INVALID", "Token de recuperación inválido o no encontrado"),
    AUTH_TOKEN_EXPIRED("AUTH_TOKEN_EXPIRED", "El enlace de recuperación ha expirado"),
    AUTH_TOKEN_GENERATION_ERROR("AUTH_TOKEN_GEN_ERROR", "Error al generar el token de recuperación. Intente nuevamente"),
    AUTH_CORREO_NOT_FOUND("AUTH_CORREO_NOT_FOUND", "Correo no encontrado en el sistema"),
    AUTH_CREDENCIALES_INVALIDAS("AUTH_CREDENCIALES", "Credenciales inválidas"),
    AUTH_USUARIO_INACTIVO("AUTH_INACTIVO", "El usuario está inactivo");

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
