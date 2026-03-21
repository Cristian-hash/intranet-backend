package com.educativa.intranet.repository;

import com.educativa.intranet.model.AlertaModeracion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertaModeracionRepository extends JpaRepository<AlertaModeracion, Long> {
    List<AlertaModeracion> findByRevisadoPorAdminFalse();
}
