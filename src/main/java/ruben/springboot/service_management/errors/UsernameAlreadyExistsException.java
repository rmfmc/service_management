package ruben.springboot.service_management.errors;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("username already exists");
    }
}
