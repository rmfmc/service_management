package ruben.springboot.service_management.models.enums;

public enum ApplianceType {
    FRIDGE("Frigorífico"),
    FREEZER("Congelador"),

    WASHING_MACHINE("Lavadora"),
    DRYER("Secadora"),
    DISHWASHER("Lavavajillas"),

    OVEN("Horno"),
    COOKTOP_CERAMIC("Vitrocerámica"),
    COOKTOP_INDUCTION("Placa de inducción"),
    GAS_COOKTOP("Cocina/placa de gas"),

    WATER_HEATER("Calentador"),
    ELECTRIC_WATER_HEATER("Termo eléctrico"),
    BOILER("Caldera"),
    HOOD("Campana extractora"),
    OTHER("Otro");

    private final String labelEs;

    ApplianceType(String labelEs) {
        this.labelEs = labelEs;
    }

    public String getLabelEs() {
        return labelEs;
    }
}
