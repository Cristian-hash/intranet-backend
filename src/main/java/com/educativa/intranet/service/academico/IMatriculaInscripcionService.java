package com.educativa.intranet.service.academico;

import com.educativa.intranet.dto.MatriculaMasivaDTO;
import com.educativa.intranet.dto.MatriculaResponseDTO;

import java.util.List;

public interface IMatriculaInscripcionService {
    List<MatriculaResponseDTO> matricularAlumnosMasivamente(MatriculaMasivaDTO dto);
}
