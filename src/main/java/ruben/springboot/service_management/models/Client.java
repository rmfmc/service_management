package ruben.springboot.service_management.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    private String name;

    private String address;

    private String city;
    
    @Column(name = "has_stairs")
    private Boolean hasStairs;

    private String phone;

    @Column(name = "phone_2")
    private String phone2;

    private String email;

    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Client(Long id, String name, String address, String city, Boolean hasStairs, String phone, String phone2,
            String email, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.hasStairs = hasStairs;
        this.phone = phone;
        this.phone2 = phone2;
        this.email = email;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Client() {
    }

    @PrePersist
    public void PrePersist(){
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getHasStairs() {
        return hasStairs;
    }

    public void setHasStairs(Boolean hasStairs) {
        this.hasStairs = hasStairs;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    

}
