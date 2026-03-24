package com.educativa.intranet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfesorPerfilCreateDTO {
    private Long usuarioId;

    @NotBlank(message = "El profesor debe tener una especialidad registrada")
    private String especialidad;
}
