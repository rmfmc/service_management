package ruben.springboot.service_management.models.enums;

public enum WorkOrderStatus {
    OPEN("Nuevo"),
    IN_PROGRESS("En progreso"),
    PENDING_PART("Pendiente de pieza"),
    PENDING_CUSTOMER("Pendiente del cliente"),
    PENDING_PAYMENT("Pendiente de pago"),
    APPLIANCE_INSTALLED("Aparato instalado"),
    CLOSED("Terminado");

    private final String labelEs;

    WorkOrderStatus(String labelEs) {
        this.labelEs = labelEs;
    }

    public String getLabelEs() {
        return labelEs;
    }

}
