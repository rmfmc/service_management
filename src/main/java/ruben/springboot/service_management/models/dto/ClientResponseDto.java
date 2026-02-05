package ruben.springboot.service_management.models.dto;

import java.time.LocalDateTime;

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
    public LocalDateTime createdAt;
    
    public ClientResponseDto(Long id, String name, String phone, String phone2, String email, String address,
            String city, Boolean hasStairs, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.phone2 = phone2;
        this.email = email;
        this.address = address;
        this.city = city;
        this.hasStairs = hasStairs;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public ClientResponseDto() {
    }

}
