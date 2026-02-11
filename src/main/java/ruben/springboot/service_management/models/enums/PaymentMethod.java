package ruben.springboot.service_management.models.enums;

public enum PaymentMethod {

    CASH("Efectivo"),
    CARD("Tarjeta"),
    BIZUM("Bizum"),
    TRANSFER("Transferencia"),
    NOT_SPECIFIED("No especificado"),
    OTHER("Otro");

    private final String labelEs;

    PaymentMethod(String labelEs) {
        this.labelEs = labelEs;
    }

    public String getLabelEs() {
        return labelEs;
    }

}
