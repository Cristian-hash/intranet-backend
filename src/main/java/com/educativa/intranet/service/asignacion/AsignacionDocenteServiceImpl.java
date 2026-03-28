package com.educativa.intranet.service.asignacion;

import com.educativa.intranet.dto.AsignacionDocenteCreateDTO;
import com.educativa.intranet.dto.AsignacionDocenteResponseDTO;
import com.educativa.intranet.model.*;
import com.educativa.intranet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsignacionDocenteServiceImpl implements IAsignacionDocenteService {

    private final AsignacionDocenteRepository asignacionRepo;
    private final ProfesorRepository profesorRepo;
    private final CursoRepository cursoRepo;
    private final PeriodoRepository periodoRepo;

    @Override
    @Transactional
    public AsignacionDocenteResponseDTO crearAsignacion(AsignacionDocenteCreateDTO dto) {
        // Validar que no exista duplicado
        if (asignacionRepo.existsByProfesorIdAndCursoIdAndPeriodoId(
                dto.getProfesorId(), dto.getCursoId(), dto.getPeriodoId())) {
            throw new RuntimeException("Esta asignación ya existe. No se puede duplicar.");
        }

        Profesor profesor = profesorRepo.findById(dto.getProfesorId())
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
        Curso curso = cursoRepo.findById(dto.getCursoId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        Periodo periodo = periodoRepo.findById(dto.getPeriodoId())
                .orElseThrow(() -> new RuntimeException("Periodo no encontrado"));

        AsignacionDocente asignacion = AsignacionDocente.builder()
                .profesor(profesor)
                .curso(curso)
                .periodo(periodo)
                .activa(true)
                .build();

        asignacionRepo.save(asignacion);
        return mapearADTO(asignacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionDocenteResponseDTO> listarTodas() {
        return asignacionRepo.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionDocenteResponseDTO> listarMisCursos(Long profesorId) {
        return asignacionRepo.findByProfesorIdAndActivaTrue(profesorId).stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void desactivarAsignacion(Long asignacionId) {
        AsignacionDocente asignacion = asignacionRepo.findById(asignacionId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        asignacion.setActiva(false);
        asignacionRepo.save(asignacion);
    }

    private AsignacionDocenteResponseDTO mapearADTO(AsignacionDocente a) {
        return AsignacionDocenteResponseDTO.builder()
                .id(a.getId())
                .profesorId(a.getProfesor().getId())
                .profesorNombre(a.getProfesor().getUsuario().getNombre())
                .cursoId(a.getCurso().getId())
                .cursoNombre(a.getCurso().getNombre())
                .grado(a.getCurso().getGrado())
                .seccion(a.getCurso().getSeccion())
                .periodoId(a.getPeriodo().getId())
                .periodoNombre(a.getPeriodo().getNombre())
                .activa(a.getActiva())
                .build();
    }
}
