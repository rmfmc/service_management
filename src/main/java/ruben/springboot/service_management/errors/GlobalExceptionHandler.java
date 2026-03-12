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
    public Map<String, Object> validation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", 400);
        body.put("errors", errors);
        return body;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, Object> handleBadJson(HttpMessageNotReadableException ex) {

        String field = "body";
        String message = "invalid request body";

        Throwable cause = ex.getMostSpecificCause();

        if (cause instanceof InvalidFormatException ife) {
            field = extractField(ife);

            Object invalidValue = ife.getValue();
            Class<?> targetType = ife.getTargetType();

            if (targetType != null && targetType.isEnum()) {
                String allowed = Arrays.stream(targetType.getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                message = "invalid value for '" + field + "': '" + invalidValue + "'. Allowed: [" + allowed + "]";
            } else {
                message = "invalid value for '" + field + "'";
            }
        } else {
            String msg = cause != null ? cause.getMessage() : ex.getMessage();

            if (msg != null && (msg.contains("Unexpected end-of-input")
                    || msg.contains("Unexpected character")
                    || msg.contains("was expecting"))) {
                message = "malformed JSON";
            }
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 400);

        Map<String, String> errors = new LinkedHashMap<>();
        errors.put(field, message);
        body.put("errors", errors);

        return body;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyExistsException.class)
    public Map<String, Object> handleUsernameExists(AlreadyExistsException ex) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", 409);

        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("message", ex.getMessage());
        body.put("errors", errors);

        return body;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public Map<String, Object> handleNotFound(NotFoundException ex) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", 404,
                "error", "NOT_FOUND",
                "message", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public Map<String, Object> handleBadRequest(UnauthorizedException ex) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", 401,
                "error", "UNAUTHORIZED",
                "message", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Map<String, Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", ex.getParameterName() + " parameter is required");
        body.put("path", request.getRequestURI());

        return body;
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Map<String, Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        body.put("error", "Method Not Allowed");
        String method = ex.getMethod();
        body.put("message", method + " is not allowed for this endpoint");
        body.put("path", request.getRequestURI());

        return body;
    }

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

}
