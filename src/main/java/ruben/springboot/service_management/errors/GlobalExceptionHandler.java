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

        String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();

        String field = "body";
        String message = "invalid request body";

        // 1) Caso típico: enum inválido (por ejemplo applianceType o role)
        // Jackson suele decir: "Cannot deserialize value of type ... from String
        // \"XXX\""
        if (msg != null && msg.contains("Cannot deserialize value of type")) {
            // intenta sacar el campo que falló si aparece
            // muchas veces aparece algo como: "through reference chain:
            // ...[\"applianceType\"]"
            int idx = msg.lastIndexOf("[\"");
            if (idx != -1) {
                int end = msg.indexOf("\"]", idx);
                if (end != -1)
                    field = msg.substring(idx + 2, end);
            }

            message = "invalid value for '" + field + "' (check allowed enum values)";
        }

        // 2) Caso: JSON vacío / mal formado
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
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public Map<String, Object> handleUsernameExists(UsernameAlreadyExistsException ex) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", 409);

        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("username", ex.getMessage());
        body.put("errors", errors);

        return body;
    }

}
