package com.educativa.intranet.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    // Relaciones 1-1 con los perfiles
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Alumno alumno;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Profesor profesor;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Padre padre;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<LogAcceso> logsAcceso;
}
