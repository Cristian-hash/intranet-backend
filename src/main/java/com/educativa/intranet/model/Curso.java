package com.educativa.intranet.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "cursos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nombre", "grado", "seccion", "anio"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre; // Ej: "Física", "Trigonometría"

    @Column(nullable = false)
    private String grado; // Ej: "1ro", "2do", "3ro", "4to", "5to"

    @Column(nullable = false)
    private String seccion; // Ej: "A", "B", "C"

    @Column(nullable = false)
    private Integer anio; // Ej: 2026

    @ManyToMany(mappedBy = "cursos")
    private List<Profesor> profesores;
}
