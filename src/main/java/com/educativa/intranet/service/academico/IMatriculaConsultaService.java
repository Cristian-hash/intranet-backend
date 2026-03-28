package com.educativa.intranet.service.academico;

import com.educativa.intranet.dto.MatriculaResponseDTO;

import java.util.List;

public interface IMatriculaConsultaService {
    List<MatriculaResponseDTO> verListaDeSalon(Long cursoId);
    List<com.educativa.intranet.dto.CursoResponseDTO> listarTodosLosCursos();
}
