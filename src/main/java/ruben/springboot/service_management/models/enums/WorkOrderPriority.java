package ruben.springboot.service_management.models.enums;

public enum WorkOrderPriority {

    LOW(1, "Baja"),
    MID(2, "Media"),
    HIGH(3, "Alta"),
    URGENT(4, "Urgente");

    private final int priority;
    private final String labelEs;

    WorkOrderPriority(int priority, String labelEs) {
        this.priority = priority;
        this.labelEs = labelEs;
    }

    public int getPriority() {
        return this.priority;
    }

    public String getLabelEs() {
        return this.labelEs;
    }

    public static String getLabelEsByInt(int priority) {
        switch (priority) {
            case 1:
                return LOW.labelEs;
            case 2:
                return MID.labelEs;
            case 3:
                return HIGH.labelEs;
            case 4:
                return URGENT.labelEs;
            default:
                return MID.labelEs;
        }
    }

}
