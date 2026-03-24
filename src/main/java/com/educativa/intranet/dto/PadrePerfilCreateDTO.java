package com.educativa.intranet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PadrePerfilCreateDTO {
    private Long usuarioId;

    @NotBlank(message = "El teléfono no puede estar vacío para contactos de emergencia")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
}
