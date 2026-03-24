package com.educativa.intranet.repository;

import com.educativa.intranet.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    
    // Buscar si un alumno ya está matriculado en ese curso para evitar choques
    Optional<Matricula> findByAlumnoIdAndCursoId(Long alumnoId, Long cursoId);

    // Listar todos los alumnos activos matriculados en un curso específico (Útil para que el profe vea su salón)
    List<Matricula> findByCursoIdAndActivoTrue(Long cursoId);
}
