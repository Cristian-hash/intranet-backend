package com.educativa.intranet.service.academico;

import com.educativa.intranet.dto.MatriculaResponseDTO;
import com.educativa.intranet.model.Matricula;
import org.springframework.stereotype.Component;

@Component
public class MatriculaMapper {

    public MatriculaResponseDTO convertirHaciaDTO(Matricula matricula) {
        return MatriculaResponseDTO.builder()
                .matriculaId(matricula.getId())
                .alumnoId(matricula.getAlumno().getId())
                .nombreAlumno(matricula.getAlumno().getUsuario().getNombre())
                .cursoNombre(matricula.getCurso().getNombre())
                .fecha(matricula.getFechaMatricula())
                .activo(matricula.getActivo())
                .build();
    }
}
