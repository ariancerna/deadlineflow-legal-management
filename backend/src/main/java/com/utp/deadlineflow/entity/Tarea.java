package com.utp.deadlineflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tarea")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTarea tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridad prioridad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTarea estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "expediente_id")
    private Expediente expediente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsable_id")
    private Usuario responsable;

    @Column(nullable = false)
    private LocalDate fechaLimite;

    @Column(nullable = false)
    private boolean confirmacionSupervisor = false;

    @Column(nullable = false)
    private boolean esDuplicada = false;
}
