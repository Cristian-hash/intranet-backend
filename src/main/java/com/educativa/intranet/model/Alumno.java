package com.educativa.intranet.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "alumnos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    private String grado; // Ej: "3ro de Secundaria"
    private java.time.LocalDate fechaNacimiento;

    // Relación asimétrica para que el Padre vea sus alumnos
    @ManyToOne
    @JoinColumn(name = "padre_id")
    private Padre padre;

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL)
    private List<Nota> notas;

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL)
    private List<Asistencia> asistencias;
}
