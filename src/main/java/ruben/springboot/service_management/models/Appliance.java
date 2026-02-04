package ruben.springboot.service_management.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ruben.springboot.service_management.models.enums.ApplianceType;

@Entity
@Table(name = "appliances")
public class Appliance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appliance_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ApplianceType applianceType;

    private String model;

    private String brand;

    @Column(name = "serial_number")
    private String serialNumber;

    private boolean active = true;

    public Appliance(Long id, Client client, ApplianceType applianceType, String model, String brand,
            String serialNumber, boolean active) {
        this.id = id;
        this.client = client;
        this.applianceType = applianceType;
        this.model = model;
        this.brand = brand;
        this.serialNumber = serialNumber;
        this.active = active;
    }

    public Appliance() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ApplianceType getApplianceType() {
        return applianceType;
    }

    public void setApplianceType(ApplianceType applianceType) {
        this.applianceType = applianceType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
