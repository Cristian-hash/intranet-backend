package com.educativa.intranet.service.asistencia;

import com.educativa.intranet.dto.AsistenciaMasivaDTO;
import com.educativa.intranet.model.Asistencia;
import com.educativa.intranet.model.Curso;
import com.educativa.intranet.model.EstadoAsistencia;
import com.educativa.intranet.model.Matricula;
import com.educativa.intranet.repository.AsistenciaRepository;
import com.educativa.intranet.repository.CursoRepository;
import com.educativa.intranet.repository.MatriculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsistenciaInscripcionServiceImpl implements IAsistenciaInscripcionService {

    private final AsistenciaRepository asistenciaRepository;
    private final MatriculaRepository matriculaRepository;
    private final CursoRepository cursoRepository;

    @Override
    @Transactional
    public void registrarAsistenciaVeloz(AsistenciaMasivaDTO dto) {
        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new RuntimeException("Ese curso no existe."));

        // Extraemos TODOS los alumnos que tienen pupitre en este curso
        List<Matricula> matriculadosActivos = matriculaRepository.findByCursoIdAndActivoTrue(curso.getId());

        // Para evitar salvar 30 asistencias una por una (Lento), usamos una lista y grabamos en bloque
        List<Asistencia> asistenciasDelDia = matriculadosActivos.stream().map(matricula -> {
            boolean falto = dto.getAlumnosAusentesIds() != null && 
                            dto.getAlumnosAusentesIds().contains(matricula.getAlumno().getId());
                            
            return Asistencia.builder()
                    .alumno(matricula.getAlumno())
                    .curso(curso)
                    .fecha(dto.getFecha())
                    .estado(falto ? EstadoAsistencia.AUSENTE : EstadoAsistencia.PRESENTE)
                    .build();
        }).collect(Collectors.toList());

        asistenciaRepository.saveAll(asistenciasDelDia); // Guardado Ultra-Rápido masivo (Batch Save)
    }
}
