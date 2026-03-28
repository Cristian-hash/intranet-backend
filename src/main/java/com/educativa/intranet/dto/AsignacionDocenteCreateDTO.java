package com.educativa.intranet.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignacionDocenteCreateDTO {

    @NotNull(message = "Debes indicar el ID del profesor")
    private Long profesorId;

    @NotNull(message = "Debes indicar el ID del curso")
    private Long cursoId;

    @NotNull(message = "Debes indicar el ID del periodo")
    private Long periodoId;
}
