package com.educativa.intranet.repository;

import com.educativa.intranet.model.Periodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeriodoRepository extends JpaRepository<Periodo, Long> {
    Optional<Periodo> findByNombre(String nombre);
}
