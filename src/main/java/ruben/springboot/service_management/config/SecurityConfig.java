package ruben.springboot.service_management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ruben.springboot.service_management.authentication.JwtAuthFilter;
import ruben.springboot.service_management.errors.RestAccessDeniedHandler;
import ruben.springboot.service_management.errors.RestAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private RestAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint) // 401
                        .accessDeniedHandler(accessDeniedHandler) // 403
                )
                .authorizeHttpRequests(auth -> auth
                        // AUTH sin token
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        // USERS solo ADMIN
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // CLIENTS: ADMIN y TECH
                        .requestMatchers("/api/clients/**").hasAnyRole("ADMIN", "TECH")

                        // WORK_ORDERS: ADMIN y TECH (luego afinamos)
                        .requestMatchers("/api/work-orders/**").hasAnyRole("ADMIN", "TECH")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
