package ruben.springboot.service_management.models.enums;

public enum ChargeType {

    VISIT("Visita"),
    REPAIR("Reparación"),
    INSTALLATION("Instalación"),
    NOT_SPECIFIED("No especificado"),
    OTHER("Otro");

    private final String labelEs;

    ChargeType(String labelEs) {
        this.labelEs = labelEs;
    }

    public String getLabelEs() {
        return labelEs;
    }

}
