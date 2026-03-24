package com.educativa.intranet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MensajeResponseDTO {
    private Long id;
    private Long emisorId;
    private String emisorNombre;
    private String contenido;
    private LocalDateTime fechaEntregado;
}
