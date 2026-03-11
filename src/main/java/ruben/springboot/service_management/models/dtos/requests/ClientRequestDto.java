package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClientRequestDto {

    @NotBlank(message = "name is required")
    @Size(max = 50, message = "name must be at most 50 characters")
    public String name;

    @NotBlank(message = "phone is required")
    @Size(min = 9, max = 12, message = "phone must be between 9 and 12 characters")
    public String phone;

    @Size(min = 9, max = 12, message = "phone2 must be between 9 and 12 characters")
    public String phone2;

    @Size(min = 9, max = 12, message = "phone3 must be between 9 and 12 characters")
    public String phone3;

    @Size(min = 9, max = 12, message = "phone4 must be between 9 and 12 characters")
    public String phone4;

    @Email(message = "email must be valid")
    @Size(max = 50, message = "email must be at most 50 characters")
    public String email;

    @Size(max = 100, message = "notes must be at most 100 characters")
    public String notes;

}
