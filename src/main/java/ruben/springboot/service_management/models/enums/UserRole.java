package ruben.springboot.service_management.models.enums;

public enum UserRole {
    
    ADMIN("Administrador"),
    TECH("Técnico");

    private final String labelEs;

    UserRole(String labelEs) {
        this.labelEs = labelEs;
    }

    public String getLabelEs() {
        return labelEs;
    }
    
}