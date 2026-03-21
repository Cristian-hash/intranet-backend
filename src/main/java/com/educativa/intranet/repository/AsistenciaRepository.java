package com.educativa.intranet.repository;

import com.educativa.intranet.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByAlumnoId(Long alumnoId);
}
