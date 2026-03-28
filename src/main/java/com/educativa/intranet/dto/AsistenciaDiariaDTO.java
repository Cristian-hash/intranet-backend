package com.educativa.intranet.dto;

import com.educativa.intranet.model.EstadoAsistencia;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AsistenciaDiariaDTO {

    @NotNull(message = "Debes indicar la fecha")
    private LocalDate fecha;

    // Solo los que faltaron. El resto se marca PRESENTE automáticamente.
    private List<Long> alumnosAusentesIds;

    // Opcional: para tardanzas
    private List<Long> alumnosTardanzaIds;

    // Grado y sección para filtrar los alumnos
    @NotNull(message = "Debes indicar el grado")
    private String grado;

    @NotNull(message = "Debes indicar la sección")
    private String seccion;
}
