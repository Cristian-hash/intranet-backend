package com.educativa.intranet.repository;

import com.educativa.intranet.model.Comunicado;
import com.educativa.intranet.model.TipoAudiencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ComunicadoRepository extends JpaRepository<Comunicado, Long> {
    
    // Consulta veloz: Trae todos los Globales MAYOR los de la audiencia especifica del usuario logueado.
    @Query("SELECT c FROM Comunicado c WHERE c.audienciaDestino = 'GLOBAL' OR c.audienciaDestino = :audiencia ORDER BY c.fechaPublicacion DESC")
    List<Comunicado> findByAudienciaOrGlobalOrderByFechaDesc(@Param("audiencia") TipoAudiencia audiencia);
}
