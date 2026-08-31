package com.utp.deadlineflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "expediente_id")
    private Expediente expediente;

    @Column(nullable = false)
    private String nombreArchivo;

    private String tipoDocumento;

    private String etiqueta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDocumento estado;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private LocalDateTime fechaCarga;
}
