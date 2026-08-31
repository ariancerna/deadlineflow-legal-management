package com.utp.deadlineflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Registro inmutable (append-only). No expone operaciones de actualización o borrado
 * a nivel de repositorio/servicio, en cumplimiento del principio de auditoría de DEADLINEFLOW.
 */
@Entity
@Table(name = "bitacora")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long entidadId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEvento tipoEvento;

    @Column(nullable = false)
    private String detalle;

    @Column(nullable = false)
    private String usuarioResponsable;

    @Column(nullable = false)
    private LocalDateTime fechaEvento;
}
