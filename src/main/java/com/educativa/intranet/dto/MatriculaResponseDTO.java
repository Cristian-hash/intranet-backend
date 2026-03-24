package com.educativa.intranet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class MatriculaResponseDTO {
    private Long matriculaId;
    private Long alumnoId;
    private String nombreAlumno;
    private String cursoNombre;
    private LocalDate fecha;
    private Boolean activo;
}
