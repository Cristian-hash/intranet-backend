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
    private Double valor;
}
