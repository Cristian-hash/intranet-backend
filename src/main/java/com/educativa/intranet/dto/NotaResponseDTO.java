package com.educativa.intranet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotaResponseDTO {
    private Long id;
    private String curso;
    private String profesor;
    private String calificacion; // "AD", "A", "B", "C"
    private String periodo;      // Nombre del periodo (ej: "1er Bimestre 2026")
}
