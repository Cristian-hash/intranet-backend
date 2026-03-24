package com.educativa.intranet.service;

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
public class AcademicoService {

    private final CursoRepository cursoRepository;
    private final AlumnoRepository alumnoRepository;
    private final MatriculaRepository matriculaRepository;

    // =========================================================
    // CASOS DE USO PRINCIPALES (Expuestos al Controller)
    // =========================================================
    
    @Transactional
    public List<MatriculaResponseDTO> matricularAlumnosMasivamente(MatriculaMasivaDTO dto) {
        Curso curso = obtenerCursoOArrojarError(dto.getCursoId());

        return dto.getAlumnoIds().stream()
                .map(alumnoId -> procesarMatriculaIndividual(alumnoId, curso))
                .filter(Objects::nonNull) // Ignoramos a los alumnos que la función de abajo rechazó por duplicados
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> verListaDeSalon(Long cursoId) {
        return matriculaRepository.findByCursoIdAndActivoTrue(cursoId).stream()
                .map(this::convertirHaciaDTO)
                .collect(Collectors.toList());
    }

    // =========================================================
    // FUNCIONES PRIVADAS UNITARIAS (Clean Code)
    // =========================================================

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
        // La responsabilidad pura de decidir qué hacer con un duplicado vive aquí.
        if (!matricula.getActivo()) {
            matricula.setActivo(true);
            matriculaRepository.save(matricula);
            return convertirHaciaDTO(matricula);
        }
        // Retornamos nulo si ya está activo. Así no agregamos copias a la respuesta JSON final.
        return null;
    }

    private MatriculaResponseDTO asentarNuevaMatricula(Alumno alumno, Curso curso) {
        Matricula nuevaMatricula = Matricula.builder()
                .alumno(alumno)
                .curso(curso)
                .build();
        matriculaRepository.save(nuevaMatricula);
        return convertirHaciaDTO(nuevaMatricula);
    }

    private MatriculaResponseDTO convertirHaciaDTO(Matricula matricula) {
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
