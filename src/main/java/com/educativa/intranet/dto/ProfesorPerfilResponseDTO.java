package com.educativa.intranet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfesorPerfilResponseDTO {
    private Long perfilId;
    private Long usuarioId;
    private String nombre;
    private String correo;
    private String especialidad;
}
