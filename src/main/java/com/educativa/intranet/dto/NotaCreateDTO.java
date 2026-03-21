package com.educativa.intranet.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotaCreateDTO {
    @NotNull(message = "ID del alumno es obligatorio")
    private Long alumnoId;

    @NotNull(message = "ID del curso es obligatorio")
    private Long cursoId;

    @NotNull(message = "El valor de la nota es obligatorio")
    @Min(value = 0, message = "La nota minima es 0")
    @Max(value = 20, message = "La nota maxima es 20")
    private Double valor;
}
