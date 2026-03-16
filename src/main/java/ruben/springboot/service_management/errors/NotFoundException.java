package ruben.springboot.service_management.errors;

public class NotFoundException extends RuntimeException {
    
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String resource, Object id) {
        super(resource + " no encontrado con id: " + id);
    }
    
}