package com.educativa.intranet.dto.comunicado;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter @Builder
public class ComunicadoResponseDTO {
    private Long id;
    private String titulo;
    private String contenido;
    private String autor; // Solo sacamos el nombre del admin
    private LocalDateTime fechaPublicacion;
    private boolean yaLoLei; // <-- ¡El dato matador para la interfaz de Angular!
}
