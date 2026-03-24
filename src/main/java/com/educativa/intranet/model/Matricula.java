package com.educativa.intranet.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "matriculas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"alumno_id", "curso_id"}) // Regla de Oro: ¡Un alumno no puede matricularse 2 veces en el mismo curso!
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Builder.Default
    private LocalDate fechaMatricula = LocalDate.now();

    @Builder.Default
    private Boolean activo = true; // Permite retirar a un alumno sin borrar su historial
}
