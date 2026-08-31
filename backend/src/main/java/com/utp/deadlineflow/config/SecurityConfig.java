package com.utp.deadlineflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Configuración RBAC. Regla R2: el rol AUDITOR solo puede ejecutar GET; cualquier
 * método mutador (POST/PUT/PATCH/DELETE) resulta en 403 Forbidden.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                          CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // El navegador envía OPTIONS antes de POST/PATCH con JSON o Authorization.
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/v1/auth/**", "/h2-console/**",
                                 "/v3/api-docs/**", "/swagger-ui/**").permitAll()

                .requestMatchers(HttpMethod.POST, "/api/v1/balance-carga/reasignar")
                    .hasAnyRole("COORDINADOR", "ADMINISTRADOR")

                // R2: AUDITOR solo lectura en todo el sistema
                .requestMatchers(HttpMethod.GET, "/api/v1/**")
                    .hasAnyRole("ASISTENTE", "ABOGADO", "COORDINADOR", "AUDITOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/api/v1/**")
                    .hasAnyRole("ASISTENTE", "ABOGADO", "COORDINADOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/api/v1/**")
                    .hasAnyRole("ASISTENTE", "ABOGADO", "COORDINADOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/**")
                    .hasAnyRole("ASISTENTE", "ABOGADO", "COORDINADOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/**").denyAll() // refuerza R1 a nivel global
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
