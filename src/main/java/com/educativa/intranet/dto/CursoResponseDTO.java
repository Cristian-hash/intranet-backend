package com.educativa.intranet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CursoResponseDTO {
    private Long id;
    private String nombre;
    private String grado;
    private String seccion;
    private Integer anio;
}
