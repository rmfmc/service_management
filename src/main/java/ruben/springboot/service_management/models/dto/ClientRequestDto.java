package ruben.springboot.service_management.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClientRequestDto {

    @NotBlank(message = "name is required")
    @Size(max = 50, message = "name must be at most 50 characters")
    public String name;

    @NotBlank(message = "address is required")
    @Size(max = 50, message = "address must be at most 50 characters")
    public String address;

    @Size(max = 45, message = "city must be at most 45 characters")
    public String city;

    public Boolean hasStairs;

    @NotBlank(message = "phone is required")
    @Size(min = 9, max = 12, message = "phone must be between 9 and 12 characters")
    public String phone;

    @Size(min = 9, max = 12, message = "phone2 must be between 9 and 12 characters")
    public String phone2;

    @Email(message = "email must be valid")
    @Size(max = 50, message = "email must be at most 50 characters")
    public String email;

    @Size(max = 100, message = "notes must be at most 100 characters")
    public String notes;

    public ClientRequestDto(String name, String address, String city, Boolean hasStairs, String phone, String phone2,
            String email, String notes) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.hasStairs = hasStairs;
        this.phone = phone;
        this.phone2 = phone2;
        this.email = email;
        this.notes = notes;
    }

    public ClientRequestDto() {
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

}
