package ruben.springboot.service_management.errors;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> validation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));

        return errorBody(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "La validación del request falló", request,
                fieldErrors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, Object> handleBadJson(HttpMessageNotReadableException ex, HttpServletRequest request) {

        String field = "body";
        String message = "cuerpo de request inválido";

        Throwable cause = ex.getMostSpecificCause();

        if (cause instanceof InvalidFormatException ife) {
            field = extractField(ife);

            Object invalidValue = ife.getValue();
            Class<?> targetType = ife.getTargetType();

            if (targetType != null && targetType.isEnum()) {
                String allowed = Arrays.stream(targetType.getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                message = "valor inválido para '" + field + "': '" + invalidValue + "'. Permitidos: [" + allowed + "]";
            } else {
                message = "valor inválido para '" + field + "'";
            }
        } else {
            String msg = cause != null ? cause.getMessage() : ex.getMessage();

            if (msg != null && (msg.contains("Unexpected end-of-input")
                    || msg.contains("Unexpected character")
                    || msg.contains("was expecting"))) {
                message = "JSON mal formado";
            }
        }

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        fieldErrors.put(field, message);

        return errorBody(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Cuerpo de request inválido", request, fieldErrors);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyExistsException.class)
    public Map<String, Object> handleAlreadyExists(AlreadyExistsException ex, HttpServletRequest request) {
        return errorBody(HttpStatus.CONFLICT, "CONFLICT", safeMessage(ex.getMessage(), "El recurso ya existe"),
                request, null);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public Map<String, Object> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return errorBody(HttpStatus.NOT_FOUND, "NOT_FOUND", safeMessage(ex.getMessage(), "Recurso no encontrado"),
                request, null);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public Map<String, Object> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        return errorBody(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED",
                safeMessage(ex.getMessage(),"Debes autenticarte para realizar esta acción"), request, null);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public Map<String, Object> handleForbidden(ForbiddenException ex, HttpServletRequest request) {
        return errorBody(HttpStatus.FORBIDDEN, "FORBIDDEN", safeMessage(ex.getMessage(), "No tienes permisos para realizar esta acción"),
                request, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ BadRequestException.class, IllegalArgumentException.class })
    public Map<String, Object> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        return errorBody(HttpStatus.BAD_REQUEST, "BAD_REQUEST", safeMessage(ex.getMessage(), "Solicitud inválida"),
                request, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Map<String, Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        return errorBody(HttpStatus.BAD_REQUEST, "BAD_REQUEST",
                "El parámetro " + ex.getParameterName() + " es obligatorio", request,
                null);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Map<String, Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {
                String method = ex.getMethod();
        return errorBody(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED",
                "El método " + method + " no está permitido para este endpoint", request, null);
    }

    // HELPER
    private String extractField(InvalidFormatException ife) {
        if (ife.getPath().isEmpty()) {
            return "body";
        }

        String raw = ife.getPath().get(ife.getPath().size() - 1).getDescription();
        if (raw == null || raw.isBlank()) {
            return "body";
        }

        int start = raw.indexOf("[\"");
        int end = raw.indexOf("\"]");

        if (start != -1 && end != -1 && end > start + 2) {
            return raw.substring(start + 2, end);
        }

        return raw;
    }

    // HELPER
    private String safeMessage(String message, String fallback) {
        if (message == null || message.isBlank()) {
            return fallback;
        }
        return message;
    }

    private Map<String, Object> errorBody(HttpStatus status, String error, String message, HttpServletRequest request,
            Map<String, String> errors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", request.getRequestURI());

        if (errors != null && !errors.isEmpty()) {
            body.put("errors", errors);
        }

        return body;
    }
}
