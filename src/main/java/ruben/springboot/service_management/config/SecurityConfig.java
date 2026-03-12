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

                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        .requestMatchers("/api/clients/**").hasRole("ADMIN")
                        
                        .requestMatchers("/api/appliances/**").hasRole("ADMIN")

                        .requestMatchers("/api/addresses/**").hasRole("ADMIN")

                        .requestMatchers("/api/brands/**").hasRole("ADMIN")

                        .requestMatchers("/api/appliance-types/**").hasRole("ADMIN")

                        .requestMatchers("/api/common-faults/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/charges/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/charges/**").hasAnyRole("ADMIN", "TECH")
                        .requestMatchers(HttpMethod.PUT, "/api/charges/**").hasAnyRole("ADMIN", "TECH")
                        .requestMatchers(HttpMethod.GET, "/api/charges").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/charges/**").hasAnyRole("ADMIN", "TECH")

                        .requestMatchers("/api/work-orders/**").hasRole("ADMIN")
                        .requestMatchers("/api/work-orders/tech/**").hasAnyRole("ADMIN", "TECH")


                        .anyRequest().permitAll())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
