package com.utp.deadlineflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "expediente")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Expediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numeroExpediente;

    @Column(nullable = false)
    private String tipoProceso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoExpediente estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private Usuario responsable;

    // Campo restringido por rol (R5) - no se expone a ASISTENTE
    private Double honorariosPactados;

    @Column(nullable = false)
    private LocalDate fechaApertura;
}
