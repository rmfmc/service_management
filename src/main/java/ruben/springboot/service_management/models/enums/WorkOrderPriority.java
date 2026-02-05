package ruben.springboot.service_management.models.enums;

public enum WorkOrderPriority {

    LOW("Baja", 1),
    MEDIUM("Media", 2),
    HIGH("Alta", 3),
    URGENT("Urgente", 4);

    private final String labelEs;
    private final int priorityInt;

    WorkOrderPriority(String labelEs, int priorityInt) {
        this.labelEs = labelEs;
        this.priorityInt = priorityInt;
    }

    public String getLabelEs() {
        return labelEs;
    }

    public int getPriorityInt() {
        return priorityInt;
    }

}
