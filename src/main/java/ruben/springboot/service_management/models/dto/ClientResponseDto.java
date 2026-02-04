package ruben.springboot.service_management.models.dto;

public class ClientResponseDto {
    
    public Long id;
    public String name;
    public String phone;
    public String phone2;
    public String email;
    public String address;
    public String city;
    public Boolean hasStairs;
    public String notes;
    
    public ClientResponseDto(Long id, String name, String phone, String phone2, String email, String address,
            String city, Boolean hasStairs, String notes) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.phone2 = phone2;
        this.email = email;
        this.address = address;
        this.city = city;
        this.hasStairs = hasStairs;
        this.notes = notes;
    }

    public ClientResponseDto() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
