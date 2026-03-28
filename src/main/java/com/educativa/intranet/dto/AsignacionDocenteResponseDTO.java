package com.educativa.intranet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AsignacionDocenteResponseDTO {
    private Long id;
    private Long profesorId;
    private String profesorNombre;
    private Long cursoId;
    private String cursoNombre;
    private String grado;
    private String seccion;
    private Long periodoId;
    private String periodoNombre;
    private Boolean activa;
}
