package com.educativa.intranet.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alertas_moderacion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AlertaModeracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mensaje_id", nullable = false)
    private Mensaje mensaje;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaRiesgo categoria;

    @Column(nullable = false)
    private String detalleAnalisis;

    @Column(nullable = false)
    private LocalDateTime fechaDeteccion;

    @Column(nullable = false)
    private Boolean revisadoPorAdmin;
}
