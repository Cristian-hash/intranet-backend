package com.educativa.intranet.service.academico;

import com.educativa.intranet.dto.MatriculaMasivaDTO;
import com.educativa.intranet.dto.MatriculaResponseDTO;
import com.educativa.intranet.model.Alumno;
import com.educativa.intranet.model.Curso;
import com.educativa.intranet.model.Matricula;
import com.educativa.intranet.repository.AlumnoRepository;
import com.educativa.intranet.repository.CursoRepository;
import com.educativa.intranet.repository.MatriculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatriculaInscripcionServiceImpl implements IMatriculaInscripcionService {

    private final CursoRepository cursoRepository;
    private final AlumnoRepository alumnoRepository;
    private final MatriculaRepository matriculaRepository;
    private final MatriculaMapper matriculaMapper; // SRP: Mapeo delegado

    @Override
    @Transactional
    public List<MatriculaResponseDTO> matricularAlumnosMasivamente(MatriculaMasivaDTO dto) {
        Curso curso = obtenerCursoOArrojarError(dto.getCursoId());

        return dto.getAlumnoIds().stream()
                .map(alumnoId -> procesarMatriculaIndividual(alumnoId, curso))
                .filter(Objects::nonNull) // Ignoramos a los rechazados por duplicados
                .collect(Collectors.toList());
    }

    private MatriculaResponseDTO procesarMatriculaIndividual(Long alumnoId, Curso curso) {
        Alumno alumno = obtenerAlumnoOArrojarError(alumnoId);
        Optional<Matricula> matriculaExistente = matriculaRepository.findByAlumnoIdAndCursoId(alumnoId, curso.getId());

        if (matriculaExistente.isPresent()) {
            return manejarMatriculaExistente(matriculaExistente.get());
        }

        return asentarNuevaMatricula(alumno, curso);
    }

    private Curso obtenerCursoOArrojarError(Long cursoId) {
        return cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Regla Académica: El curso indicado no existe en la BD."));
    }

    private Alumno obtenerAlumnoOArrojarError(Long alumnoId) {
        return alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new RuntimeException("Ese perfil de Alumno (ID: " + alumnoId + ") no existe."));
    }

    private MatriculaResponseDTO manejarMatriculaExistente(Matricula matricula) {
        if (!matricula.getActivo()) {
            matricula.setActivo(true);
            matriculaRepository.save(matricula);
            return matriculaMapper.convertirHaciaDTO(matricula);
        }
        return null;
    }

    private MatriculaResponseDTO asentarNuevaMatricula(Alumno alumno, Curso curso) {
        Matricula nuevaMatricula = Matricula.builder()
                .alumno(alumno)
                .curso(curso)
                .build();
        matriculaRepository.save(nuevaMatricula);
        return matriculaMapper.convertirHaciaDTO(nuevaMatricula);
    }
}
