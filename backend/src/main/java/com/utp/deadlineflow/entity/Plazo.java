package com.utp.deadlineflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "plazo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Plazo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "expediente_id")
    private Expediente expediente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPlazo estado;

    @Column(nullable = false)
    private LocalDate fechaLimite;

    private String motivoAnulacion;

    // Constructor de conveniencia usado en pruebas unitarias
    public Plazo(Long id, EstadoPlazo estado, LocalDate fechaLimite) {
        this.id = id;
        this.estado = estado;
        this.fechaLimite = fechaLimite;
    }
}
