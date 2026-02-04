package ruben.springboot.service_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityBeansConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                // API REST: sin sesiones
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // API REST: normalmente desactivas CSRF (no usas cookies de sesión)
                .csrf(csrf -> csrf.disable())

                // Para que NO intente mostrar login HTML
                .formLogin(form -> form.disable())
                .httpBasic(Customizer.withDefaults()) // opcional, útil para probar, luego se quita con JWT

                .authorizeHttpRequests(auth -> auth
                        // permitimos crear usuarios y (futuro) login
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        // TODO: mientras desarrollas puedes permitir todo:
                        .anyRequest().permitAll()

                        // o si prefieres: lo demás bloqueado
                        //.anyRequest().authenticated()
                )
                .build();
    }



}
