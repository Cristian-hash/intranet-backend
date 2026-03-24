package com.educativa.intranet.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "padres")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Padre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    private String telefono;
    private String direccion;

    @OneToMany(mappedBy = "padre")
    private List<Alumno> hijos;
}
