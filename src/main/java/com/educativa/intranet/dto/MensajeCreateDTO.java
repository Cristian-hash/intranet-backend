package com.educativa.intranet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MensajeCreateDTO {
    @NotNull(message = "El receptor es obligatorio")
    private Long receptorId;

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String contenido;
}
