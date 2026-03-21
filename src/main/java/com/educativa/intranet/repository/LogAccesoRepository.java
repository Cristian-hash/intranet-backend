package com.educativa.intranet.repository;

import com.educativa.intranet.model.LogAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LogAccesoRepository extends JpaRepository<LogAcceso, Long> {
    List<LogAcceso> findByUsuarioId(Long usuarioId);
}
