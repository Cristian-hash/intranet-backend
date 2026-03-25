package com.educativa.intranet.service.asistencia;

import com.educativa.intranet.dto.AsistenciaCalendarioDTO;
import com.educativa.intranet.model.Asistencia;
import org.springframework.stereotype.Component;

@Component
public class AsistenciaMapper {
    public AsistenciaCalendarioDTO convertirHaciaDTO(Asistencia asist) {
        return AsistenciaCalendarioDTO.builder()
                .fecha(asist.getFecha())
                .dia(asist.getFecha().getDayOfMonth())
                .estado(asist.getEstado().name())
                .build();
    }
}
