package ruben.springboot.service_management.models.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClientRequestDto {

    @NotBlank(message = "el nombre es obligatorio")
    @Size(max = 50, message = "el nombre debe tener como máximo 50 caracteres")
    public String name;

    @NotBlank(message = "el teléfono es obligatorio")
    @Size(min = 9, max = 12, message = "el teléfono debe tener entre 9 y 12 caracteres")
    public String phone;

    @Size(min = 9, max = 12, message = "el teléfono 2 debe tener entre 9 y 12 caracteres")
    public String phone2;

    @Size(min = 9, max = 12, message = "el teléfono 3 debe tener entre 9 y 12 caracteres")
    public String phone3;

    @Size(min = 9, max = 12, message = "el teléfono 4 debe tener entre 9 y 12 caracteres")
    public String phone4;

    @Email(message = "el correo electrónico debe ser válido")
    @Size(max = 50, message = "el correo electrónico debe tener como máximo 50 caracteres")
    public String email;

    @Size(max = 100, message = "las notas deben tener como máximo 100 caracteres")
    public String notes;

}
