package ruben.springboot.service_management.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
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

    private String phone;

    @Column(name = "phone_2")
    private String phone2;

    @Column(name = "phone_3")
    private String phone3;

    @Column(name = "phone_4")
    private String phone4;

    private String email;

    private String notes;

    @OneToMany(mappedBy = "client")
    @OrderBy("id ASC")
    private List<Address> addresses = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Client(Long id, String name, String phone, String phone2, String phone3, String phone4, String email,
            String notes, List<Address> addresses, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.phone2 = phone2;
        this.phone3 = phone3;
        this.phone4 = phone4;
        this.email = email;
        this.notes = notes;
        this.addresses = addresses;
        this.createdAt = createdAt;
    }

    public Client(Long id) {
        this.id = id;
    }

    public Client() {
    }

    @PrePersist
    public void PrePersist() {
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

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getPhone4() {
        return phone4;
    }

    public void setPhone4(String phone4) {
        this.phone4 = phone4;
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

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Client other = (Client) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (phone == null) {
            if (other.phone != null)
                return false;
        } else if (!phone.equals(other.phone))
            return false;
        if (phone2 == null) {
            if (other.phone2 != null)
                return false;
        } else if (!phone2.equals(other.phone2))
            return false;
        if (phone3 == null) {
            if (other.phone3 != null)
                return false;
        } else if (!phone3.equals(other.phone3))
            return false;
        if (phone4 == null) {
            if (other.phone4 != null)
                return false;
        } else if (!phone4.equals(other.phone4))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (notes == null) {
            if (other.notes != null)
                return false;
        } else if (!notes.equals(other.notes))
            return false;
        return true;
    }

}
