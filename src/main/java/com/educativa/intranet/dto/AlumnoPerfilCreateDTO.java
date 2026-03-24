package com.educativa.intranet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class AlumnoPerfilCreateDTO {
    // Angular manda el usuario ID de la persona al crear su perfil
    private Long usuarioId;
    
    // Opcionalmente podemos ligarlo a un Padre desde este momento
    private Long padreId;

    @NotBlank(message = "Debes asignar un grado al estudiante")
    private String grado;

    private LocalDate fechaNacimiento;
}
