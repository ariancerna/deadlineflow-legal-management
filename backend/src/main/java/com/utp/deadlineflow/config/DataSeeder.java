package com.utp.deadlineflow.config;

import com.utp.deadlineflow.entity.Rol;
import com.utp.deadlineflow.entity.Usuario;
import com.utp.deadlineflow.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Carga de datos iniciales para desarrollo.
 * Inserta usuarios de prueba con diferentes roles si la tabla está vacía.
 */
@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner cargarDatosIniciales(UsuarioRepository usuarioRepository,
                                           PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(Usuario.builder()
                        .email("admin@deadlineflow.com")
                        .password(passwordEncoder.encode("admin123"))
                        .nombre("Administrador")
                        .rol(Rol.ADMINISTRADOR)
                        .activo(true)
                        .build());

                usuarioRepository.save(Usuario.builder()
                        .email("coordinador@deadlineflow.com")
                        .password(passwordEncoder.encode("coord123"))
                        .nombre("Coordinador Legal")
                        .rol(Rol.COORDINADOR)
                        .activo(true)
                        .build());

                usuarioRepository.save(Usuario.builder()
                        .email("abogado@deadlineflow.com")
                        .password(passwordEncoder.encode("abog123"))
                        .nombre("Abogado Pérez")
                        .rol(Rol.ABOGADO)
                        .activo(true)
                        .build());

                usuarioRepository.save(Usuario.builder()
                        .email("asistente@deadlineflow.com")
                        .password(passwordEncoder.encode("asist123"))
                        .nombre("Asistente García")
                        .rol(Rol.ASISTENTE)
                        .activo(true)
                        .build());

                usuarioRepository.save(Usuario.builder()
                        .email("auditor@deadlineflow.com")
                        .password(passwordEncoder.encode("audit123"))
                        .nombre("Auditor López")
                        .rol(Rol.AUDITOR)
                        .activo(true)
                        .build());

                System.out.println("✅ Datos iniciales cargados: 5 usuarios de prueba creados");
            }
        };
    }
}
