package com.educativa.intranet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class AsistenciaCalendarioDTO {
    private LocalDate fecha; // Ej: "2026-02-05"
    private int dia;         // Ej: 5 (Para que el calendario front-end pinte la celda exacta)
    private String estado;   // Ej: "PRESENTE" (check azul) o "AUSENTE" (cruz roja)
}
