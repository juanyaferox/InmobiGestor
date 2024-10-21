package com.feroxdev.inmobigestor.exception;

public class Exception extends RuntimeException {

    private String errorCode;  // Código o tipo de error (por ejemplo, "NOT_FOUND", "VALIDATION_ERROR")

    // Constructor general para excepciones con mensaje
    public Exception(String message) {
        super(message);
    }

    // Constructor que incluye un código de error
    public Exception(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    // Métodos de fábrica para cada tipo de excepción

    // 1. Excepción para Entidad no encontrada
    public static Exception entityNotFound(String entityName, Long id) {
        return new Exception("NOT_FOUND", entityName + " with ID " + id + " was not found.");
    }

    // 2. Excepción para Operación no permitida
    public static Exception operationNotAllowed(String operation, String reason) {
        return new Exception("OPERATION_NOT_ALLOWED", "Operation '" + operation + "' is not allowed: " + reason);
    }

    // 3. Excepción de Validación de datos
    public static Exception validationError(String fieldName, String message) {
        return new Exception("VALIDATION_ERROR", "Invalid value for field '" + fieldName + "': " + message);
    }

    // 4. Excepción para Relaciones no válidas
    public static Exception invalidRelationship(String message) {
        return new Exception("INVALID_RELATIONSHIP", message);
    }

    // 5. Excepción de Duplicación de datos
    public static Exception duplicateEntity(String entityName, String fieldName, String fieldValue) {
        return new Exception("DUPLICATE_ENTITY", entityName + " with " + fieldName + " '" + fieldValue + "' already exists.");
    }

    // 6. Excepción de Recurso no disponible
    public static Exception resourceUnavailable(String resourceName) {
        return new Exception("RESOURCE_UNAVAILABLE", resourceName + " is currently unavailable.");
    }

    // 7. Excepción de Autenticación
    public static Exception authenticationError(String message) {
        return new Exception("AUTHENTICATION_ERROR", "Authentication error: " + message);
    }

    // 8. Excepción de Autorización
    public static Exception authorizationError(String message) {
        return new Exception("AUTHORIZATION_ERROR", "Authorization error: " + message);
    }

    // Obtener el código de error
    public String getErrorCode() {
        return errorCode;
    }
}
