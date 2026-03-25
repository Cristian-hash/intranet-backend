package com.educativa.intranet.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AsistenciaMasivaDTO {
    
    @NotNull(message = "Debes indicar la fecha de la clase")
    private LocalDate fecha;

    @NotNull(message = "Debes indicar el curso donde tomas lista")
    private Long cursoId;

    // MAGIA PURA: Solo mandas a los que faltaron. El servidor de Java
    // calculará automáticamente que el resto de asistentes están Presentes.
    private List<Long> alumnosAusentesIds;
}
