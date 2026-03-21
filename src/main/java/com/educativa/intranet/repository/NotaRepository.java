package com.educativa.intranet.repository;

import com.educativa.intranet.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByAlumnoId(Long alumnoId);
    List<Nota> findByCursoIdAndProfesorId(Long cursoId, Long profesorId);
}
