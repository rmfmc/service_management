package ruben.springboot.service_management.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ruben.springboot.service_management.errors.UnauthorizedException;
import ruben.springboot.service_management.models.User;
import ruben.springboot.service_management.repositories.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String loginAndGetToken(String username, String rawPassword) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!user.isActive()) {
            throw new UnauthorizedException("User is disabled");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return jwtService.generateToken(user);
    }

    public User findByUsernameIgnoreCase(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
    }

}
