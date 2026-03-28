package com.educativa.intranet.repository;

import com.educativa.intranet.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    List<Alumno> findByGradoAndSeccion(String grado, String seccion);
}
