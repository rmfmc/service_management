package ruben.springboot.service_management.models.enums;

public enum WorkOrderPriority {

    LOW(1),
    MEDIUM(2),
    HIGH(3),
    URGENT(4);

    private final int value;

    WorkOrderPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
}
