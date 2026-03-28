package com.educativa.intranet.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Nota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_id")
    private Periodo periodo;

    @Column(nullable = false, length = 2)
    private String calificacion; // "AD", "A", "B", "C" — validado en el Service
}
