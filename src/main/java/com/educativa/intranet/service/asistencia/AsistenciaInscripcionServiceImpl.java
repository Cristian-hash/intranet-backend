package com.educativa.intranet.service.asistencia;

import com.educativa.intranet.dto.AsistenciaDiariaDTO;
import com.educativa.intranet.model.Alumno;
import com.educativa.intranet.model.Asistencia;
import com.educativa.intranet.model.EstadoAsistencia;
import com.educativa.intranet.model.Usuario;
import com.educativa.intranet.repository.AlumnoRepository;
import com.educativa.intranet.repository.AsistenciaRepository;
import com.educativa.intranet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsistenciaInscripcionServiceImpl implements IAsistenciaInscripcionService {

    private final AsistenciaRepository asistenciaRepository;
    private final AlumnoRepository alumnoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void registrarAsistenciaDiaria(AsistenciaDiariaDTO dto, Long auxiliarUsuarioId) {
        Usuario auxiliar = usuarioRepository.findById(auxiliarUsuarioId)
                .orElseThrow(() -> new RuntimeException("Auxiliar no encontrado"));

        // Obtener TODOS los alumnos del grado y sección
        List<Alumno> alumnosDelSalon = alumnoRepository.findByGradoAndSeccion(
                dto.getGrado(), dto.getSeccion());

        if (alumnosDelSalon.isEmpty()) {
            throw new RuntimeException("No hay alumnos registrados en " + dto.getGrado() + " " + dto.getSeccion());
        }

        // Sets para búsqueda O(1)
        Set<Long> ausentes = dto.getAlumnosAusentesIds() != null 
                ? new HashSet<>(dto.getAlumnosAusentesIds()) : Set.of();
        Set<Long> tardanzas = dto.getAlumnosTardanzaIds() != null 
                ? new HashSet<>(dto.getAlumnosTardanzaIds()) : Set.of();

        List<Asistencia> asistenciasDelDia = alumnosDelSalon.stream().map(alumno -> {
            EstadoAsistencia estado;
            if (ausentes.contains(alumno.getId())) {
                estado = EstadoAsistencia.AUSENTE;
            } else if (tardanzas.contains(alumno.getId())) {
                estado = EstadoAsistencia.TARDANZA;
            } else {
                estado = EstadoAsistencia.PRESENTE;
            }

            return Asistencia.builder()
                    .alumno(alumno)
                    .fecha(dto.getFecha())
                    .estado(estado)
                    .registradoPor(auxiliar)
                    .build();
        }).collect(Collectors.toList());

        asistenciaRepository.saveAll(asistenciasDelDia);
    }

    @Override
    @Transactional
    public void corregirAsistencia(Long asistenciaId, String nuevoEstado, Long auxiliarUsuarioId) {
        Asistencia asistencia = asistenciaRepository.findById(asistenciaId)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));

        Usuario auxiliar = usuarioRepository.findById(auxiliarUsuarioId)
                .orElseThrow(() -> new RuntimeException("Auxiliar no encontrado"));

        asistencia.setEstado(EstadoAsistencia.valueOf(nuevoEstado));
        asistencia.setCorregidoPor(auxiliar);
        asistenciaRepository.save(asistencia);
    }
}
