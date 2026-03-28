package com.educativa.intranet.service;

import com.educativa.intranet.dto.NotaCreateDTO;
import com.educativa.intranet.model.*;
import com.educativa.intranet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfesorService {
    
    private final NotaRepository notaRepository;
    private final AlumnoRepository alumnoRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PeriodoRepository periodoRepository;
    private final AsignacionDocenteRepository asignacionDocenteRepository;

    // Calificaciones válidas por bimestre
    private static final Set<String> LETRAS_BIMESTRE_1_2_3 = Set.of("A", "B", "C");
    private static final Set<String> LETRAS_BIMESTRE_4 = Set.of("AD", "A", "B", "C");

    @Transactional
    public Nota registrarNota(Long usuarioId, NotaCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario autenticado no existe"));
            
        Profesor profesor = usuario.getProfesor();
        if (profesor == null) {
            throw new RuntimeException("El usuario actual no posee un perfil de profesor válido");
        }

        Curso curso = cursoRepository.findById(dto.getCursoId())
            .orElseThrow(() -> new RuntimeException("El curso indicado no existe"));

        // REGLA: Validar que el profesor tiene asignación activa para este curso
        boolean tieneAsignacion = asignacionDocenteRepository
            .existsByProfesorIdAndCursoIdAndActivaTrue(profesor.getId(), curso.getId());
        if (!tieneAsignacion) {
            throw new RuntimeException("Acceso Denegado: Usted no tiene asignación activa para el curso " + curso.getNombre());
        }

        // REGLA: Validar que el periodo está activo
        Periodo periodo = periodoRepository.findById(dto.getPeriodoId())
            .orElseThrow(() -> new RuntimeException("El periodo indicado no existe"));
        if (!periodo.getActivo()) {
            throw new RuntimeException("El periodo " + periodo.getNombre() + " está cerrado. No se pueden registrar notas.");
        }

        // REGLA: Validar calificación por bimestre (AD solo en 4to bimestre)
        validarCalificacionPorBimestre(dto.getCalificacion(), periodo);

        Alumno alumno = alumnoRepository.findById(dto.getAlumnoId())
            .orElseThrow(() -> new RuntimeException("El alumno indicado no existe"));

        Nota nota = Nota.builder()
                .alumno(alumno)
                .curso(curso)
                .profesor(profesor)
                .periodo(periodo)
                .calificacion(dto.getCalificacion())
                .build();
                
        return notaRepository.save(nota);
    }

    private void validarCalificacionPorBimestre(String calificacion, Periodo periodo) {
        boolean esCuartoBimestre = periodo.getNombre().toLowerCase().contains("4to")
                               || periodo.getNombre().toLowerCase().contains("4°")
                               || periodo.getNombre().toLowerCase().contains("cuarto");
        Set<String> permitidas = esCuartoBimestre ? LETRAS_BIMESTRE_4 : LETRAS_BIMESTRE_1_2_3;
        if (!permitidas.contains(calificacion)) {
            throw new RuntimeException(
                "Calificación '" + calificacion + "' no permitida en " + periodo.getNombre() + 
                ". Opciones válidas: " + permitidas
            );
        }
    }
}
