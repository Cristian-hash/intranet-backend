package com.educativa.intranet.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "profesores")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    // Relación M-M que Hibernate convertirá en la tabla "profesor_curso" (profesor_id, curso_id)
    @ManyToMany
    @JoinTable(
        name = "profesor_curso",
        joinColumns = @JoinColumn(name = "profesor_id"),
        inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private List<Curso> cursos;
}
