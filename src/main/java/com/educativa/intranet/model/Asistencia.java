package com.educativa.intranet.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "asistencias", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"alumno_id", "fecha"}) // 1 asistencia por alumno por día
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAsistencia estado;

    // Auditoría: quién registró la asistencia (Auxiliar típicamente)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrado_por")
    private Usuario registradoPor;

    // Auditoría: quién corrigió (null si nunca se corrigió)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corregido_por")
    private Usuario corregidoPor;
}
