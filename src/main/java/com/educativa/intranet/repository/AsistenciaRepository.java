package com.educativa.intranet.repository;

import com.educativa.intranet.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByAlumnoId(Long alumnoId);

    @Query("SELECT a FROM Asistencia a WHERE a.alumno.usuario.id = :usuarioId AND YEAR(a.fecha) = :anio AND MONTH(a.fecha) = :mes ORDER BY a.fecha ASC")
    List<Asistencia> findByUsuarioAndMesExacto(@Param("usuarioId") Long usuarioId, @Param("anio") int anio, @Param("mes") int mes);
}
