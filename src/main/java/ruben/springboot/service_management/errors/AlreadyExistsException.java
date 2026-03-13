package ruben.springboot.service_management.errors;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String message) {
        super(message);
    }
    
    public AlreadyExistsException(String resource, Object value) {
        super(resource + " ya existe: " + value);
    }
    
}
