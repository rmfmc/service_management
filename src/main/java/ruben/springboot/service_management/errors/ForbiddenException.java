package ruben.springboot.service_management.errors;

public class ForbiddenException extends RuntimeException{

    public ForbiddenException() {
        super("No tienes permisos para realizar esta acción");
    }

    public ForbiddenException(String message) {
        super(message);
    }

}
