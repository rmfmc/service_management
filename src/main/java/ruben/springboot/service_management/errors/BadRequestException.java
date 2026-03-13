package ruben.springboot.service_management.errors;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String required) {
        super("Campo '" + required + "' es requerido");
    }

    public BadRequestException(String required, String missing) {
        super("Campo '" + required + "' es requerido cuando no hay id de " + missing);
    }

    public BadRequestException(String message, boolean other) {
        super(message);
    }

}
