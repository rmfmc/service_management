package ruben.springboot.service_management.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequestDto {

    @NotBlank(message = "el nombre de usuario es obligatorio")
    @Size(min = 3, max = 20, message = "el nombre de usuario debe tener entre 3 y 20 caracteres")
    private String username;

     @NotBlank(message = "la contraseña es obligatoria")
    @Size(min = 6, message = "la contraseña debe tener al menos 6 caracteres")
    private String password;

    public LoginRequestDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
