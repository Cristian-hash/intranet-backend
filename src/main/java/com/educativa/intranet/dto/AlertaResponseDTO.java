package com.educativa.intranet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AlertaResponseDTO {
    private Long alertaId;
    private String emisorNombre;
    private String receptorNombre;
    private String contenidoMensaje; // Lo que dijeron exactamente, para evaluar si era coima o acoso.
    private String categoriaRiesgo;
    private String detalleAnalisis;
    private LocalDateTime fechaDeteccion;
}
