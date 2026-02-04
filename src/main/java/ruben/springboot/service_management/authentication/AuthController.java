package ruben.springboot.service_management.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ruben.springboot.service_management.authentication.dto.LoginRequestDto;
import ruben.springboot.service_management.authentication.dto.LoginResponseDto;
import ruben.springboot.service_management.models.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto req) {

        String token = authService.loginAndGetToken(req.getUsername(), req.getPassword());

        User user = authService.findByUsernameIgnoreCase(req.getUsername());

        return new LoginResponseDto(
                token,
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );
    }

}
