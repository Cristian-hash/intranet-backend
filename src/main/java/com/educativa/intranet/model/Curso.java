package com.educativa.intranet.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "cursos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToMany(mappedBy = "cursos")
    private List<Profesor> profesores;
}
