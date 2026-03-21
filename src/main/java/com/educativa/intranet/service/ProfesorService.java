package com.educativa.intranet.service;

import com.educativa.intranet.dto.NotaCreateDTO;
import com.educativa.intranet.model.*;
import com.educativa.intranet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfesorService {
    
    private final NotaRepository notaRepository;
    private final AlumnoRepository alumnoRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

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
        
        // REGLA CLAVE: El profesor solo puede calificar un curso si lo tiene asignado
        boolean ensenaCurso = profesor.getCursos().stream()
            .anyMatch(c -> c.getId().equals(curso.getId()));
            
        if (!ensenaCurso) {
            throw new RuntimeException("Acceso Denegado: Usted no dicta el curso de " + curso.getNombre());
        }

        Alumno alumno = alumnoRepository.findById(dto.getAlumnoId())
            .orElseThrow(() -> new RuntimeException("El alumno indicado no existe"));

        Nota nota = Nota.builder()
                .alumno(alumno)
                .curso(curso)
                .profesor(profesor)
                .valor(dto.getValor())
                .build();
                
        return notaRepository.save(nota);
    }
}
