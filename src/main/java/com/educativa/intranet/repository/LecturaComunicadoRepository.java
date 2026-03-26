package com.educativa.intranet.repository;

import com.educativa.intranet.model.LecturaComunicado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturaComunicadoRepository extends JpaRepository<LecturaComunicado, Long> {
    boolean existsByComunicadoIdAndLectorId(Long comunicadoId, Long lectorId);
}
