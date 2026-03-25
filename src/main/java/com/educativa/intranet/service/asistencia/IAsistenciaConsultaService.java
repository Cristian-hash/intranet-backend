package com.educativa.intranet.service.asistencia;

import com.educativa.intranet.dto.AsistenciaCalendarioDTO;
import java.util.List;

public interface IAsistenciaConsultaService {
    List<AsistenciaCalendarioDTO> obtenerCalendarioMensual(Long usuarioId, int anio, int mes);
}
