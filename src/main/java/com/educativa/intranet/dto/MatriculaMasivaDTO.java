package com.educativa.intranet.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatriculaMasivaDTO {
    
    @NotNull(message = "Debes indicar el ID del curso")
    private Long cursoId;

    @NotEmpty(message = "La lista de alumnos a matricular no puede estar vacía")
    private List<Long> alumnoIds;
}
