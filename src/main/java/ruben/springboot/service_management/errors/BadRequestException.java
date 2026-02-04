package ruben.springboot.service_management.errors;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
