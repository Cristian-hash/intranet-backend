package com.educativa.intranet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class AlumnoPerfilResponseDTO {
    private Long perfilId;
    private Long usuarioId;
    private String nombre; // Lo sacamos del Usuario para no enviar la tabla vacía
    private String correo;
    private String grado;
    private LocalDate fechaNacimiento;
    private String nombrePadre; // Opcional para mostrar en la grilla de Angular
}
