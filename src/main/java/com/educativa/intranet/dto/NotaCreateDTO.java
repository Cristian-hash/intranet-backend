package com.educativa.intranet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotaCreateDTO {
    @NotNull(message = "ID del alumno es obligatorio")
    private Long alumnoId;

    @NotNull(message = "ID del curso es obligatorio")
    private Long cursoId;

    @NotNull(message = "ID del periodo es obligatorio")
    private Long periodoId;

    @NotBlank(message = "La calificación es obligatoria")
    @Pattern(regexp = "^(AD|A|B|C)$", message = "Calificación debe ser AD, A, B o C")
    private String calificacion; // "AD", "A", "B", "C"
}
