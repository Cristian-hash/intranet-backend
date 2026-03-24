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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AcademicoService {

    private final CursoRepository cursoRepository;
    private final AlumnoRepository alumnoRepository;
    private final MatriculaRepository matriculaRepository;

    @Transactional
    public List<MatriculaResponseDTO> matricularAlumnosMasivamente(MatriculaMasivaDTO dto) {
        // 1. Verificar si existe el salón de clases (Curso)
        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new RuntimeException("Regla Académica: El curso indicado no existe en la BD."));

        List<MatriculaResponseDTO> matrículasExitosas = new ArrayList<>();

        // 2. Recorremos tu saco de Alumnos (IDs que Angular te mandó)
        for (Long alumnoId : dto.getAlumnoIds()) {
            Alumno alumno = alumnoRepository.findById(alumnoId)
                    .orElseThrow(() -> new RuntimeException("Ese perfil de Alumno (ID: " + alumnoId + ") no existe."));

            // ¡REGLA DE ORO DE MATRÍCULAS! : Validar duplicados.
            Optional<Matricula> matriculaExistente = matriculaRepository.findByAlumnoIdAndCursoId(alumnoId, curso.getId());
            
            if (matriculaExistente.isPresent()) {
                // Si ya está matriculado, y está inactivo (retirado), se puede reactivar.
                Matricula m = matriculaExistente.get();
                if (!m.getActivo()) {
                    m.setActivo(true);
                    matriculaRepository.save(m);
                    matrículasExitosas.add(mapearDTO(m));
                }
                // Si está activo, simplemente saltamos (o lanzamos error según la regla de negocio).
                // Aquí optamos por ignorar duplicados para no romper el bucle entero.
                continue; 
            }

            // 3. Crear Ficha de Matrícula (Entity puro)
            Matricula nuevaMatricula = Matricula.builder()
                    .alumno(alumno)
                    .curso(curso)
                    .build();

            matriculaRepository.save(nuevaMatricula);
            matrículasExitosas.add(mapearDTO(nuevaMatricula));
        }

        return matrículasExitosas;
    }

    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> verListaDeSalon(Long cursoId) {
        // Retorna todos los estudiantes metidos en "Matemáticas de 3er Año"
        return matriculaRepository.findByCursoIdAndActivoTrue(cursoId).stream()
                .map(this::mapearDTO)
                .toList();
    }

    private MatriculaResponseDTO mapearDTO(Matricula m) {
        return MatriculaResponseDTO.builder()
                .matriculaId(m.getId())
                .alumnoId(m.getAlumno().getId())
                .nombreAlumno(m.getAlumno().getUsuario().getNombre()) // Saltamos dos puentes gracias a Hibernate!
                .cursoNombre(m.getCurso().getNombre())
                .fecha(m.getFechaMatricula())
                .activo(m.getActivo())
                .build();
    }
}
