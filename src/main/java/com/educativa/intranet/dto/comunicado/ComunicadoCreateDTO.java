package com.educativa.intranet.dto.comunicado;

import com.educativa.intranet.model.TipoAudiencia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ComunicadoCreateDTO {
    
    @NotBlank(message = "Debes proveer un título urgente")
    private String titulo;

    @NotBlank(message = "No puedes enviar un comunicado vacío")
    private String contenido;

    @NotNull(message = "Define si va a PADRES, PROFESORES, ALUMNOS o GLOBAL")
    private TipoAudiencia audienciaDestino;

    // Opcional: "3er Grado"
    private String gradoDestino;
}
