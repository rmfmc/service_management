package ruben.springboot.service_management.models.dtos.responses;

import java.time.LocalDateTime;
import java.util.List;

public class ClientResponseDto {
    
    public Long id;
    public String name;
    public String phone;
    public String phone2;
    public String phone3;
    public String phone4;
    public String email;
    public String notes;
    public LocalDateTime createdAt;

    public List<AddressResponseDto> addresses;

}
