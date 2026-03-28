package com.educativa.intranet.repository;

import com.educativa.intranet.model.AsignacionDocente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AsignacionDocenteRepository extends JpaRepository<AsignacionDocente, Long> {

    // El profesor consulta SUS asignaciones activas
    List<AsignacionDocente> findByProfesorIdAndActivaTrue(Long profesorId);

    // Validar si existe una asignación para un profesor+curso+periodo (evitar duplicados)
    boolean existsByProfesorIdAndCursoIdAndPeriodoId(Long profesorId, Long cursoId, Long periodoId);

    // Verificar si el profesor tiene permiso sobre un curso específico
    boolean existsByProfesorIdAndCursoIdAndActivaTrue(Long profesorId, Long cursoId);
}
