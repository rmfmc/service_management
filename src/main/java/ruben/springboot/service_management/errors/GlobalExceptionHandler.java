package ruben.springboot.service_management.errors;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> validation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 400);
        body.put("errors", errors);
        return body;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, Object> handleBadJson(HttpMessageNotReadableException ex) {

        // Dato fuera del ENUM
        String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();

        String field = "body";
        String message = "invalid request body";
        if (msg != null && msg.contains("Cannot deserialize value of type")) {
            // 1) sacar el nombre del enum: ...enums.WorkOrderStatus`
            String enumName = null;
            int t1 = msg.indexOf("`");
            int t2 = (t1 != -1) ? msg.indexOf("`", t1 + 1) : -1;
            if (t1 != -1 && t2 != -1) {
                String fullType = msg.substring(t1 + 1, t2); // ruben...WorkOrderStatus
                int lastDot = fullType.lastIndexOf(".");
                enumName = (lastDot != -1) ? fullType.substring(lastDot + 1) : fullType;
                // campo aproximado (si quieres): workOrderStatus
                field = Character.toLowerCase(enumName.charAt(0)) + enumName.substring(1);
            }

            // 2) valor inválido: from String "OPEL"
            String invalidValue = null;
            String from = "from String \"";
            int v1 = msg.indexOf(from);
            if (v1 != -1) {
                int v2 = msg.indexOf("\"", v1 + from.length());
                if (v2 != -1)
                    invalidValue = msg.substring(v1 + from.length(), v2);
            }

            // 3) valores permitidos: Enum class: [OPEN, CLOSED, ...]
            String allowed = null;
            String marker = "Enum class: [";
            int a1 = msg.indexOf(marker);
            if (a1 != -1) {
                int a2 = msg.indexOf("]", a1 + marker.length());
                if (a2 != -1)
                    allowed = msg.substring(a1 + marker.length(), a2);
            }

            // 4) construir mensaje final
            if (invalidValue != null && allowed != null) {
                message = "invalid value for '" + field + "': '" + invalidValue + "'. Allowed: [" + allowed + "]";
            } else if (allowed != null) {
                message = "invalid value for '" + field + "'. Allowed: [" + allowed + "]";
            } else {
                message = "invalid value for '" + field + "'";
            }
        }

        // JSON vacío / mal formado
        if (msg != null && (msg.contains("Unexpected end-of-input") || msg.contains("Unexpected character"))) {
            field = "body";
            message = "malformed JSON";
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
                "status", 404,
                "error", "NOT_FOUND",
                "message", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public Map<String, Object> handleBadRequest(UnauthorizedException ex) {
        return Map.of(
                "status", 401,
                "error", "UNAUTHORIZED",
                "message", ex.getMessage());
    }

}
