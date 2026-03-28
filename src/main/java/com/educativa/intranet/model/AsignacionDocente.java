package com.educativa.intranet.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asignaciones_docentes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"profesor_id", "curso_id", "periodo_id"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AsignacionDocente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso; // Curso YA incluye grado + sección + año

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_id", nullable = false)
    private Periodo periodo;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activa = true; // Para reemplazos: desactivar sin borrar historial
}
