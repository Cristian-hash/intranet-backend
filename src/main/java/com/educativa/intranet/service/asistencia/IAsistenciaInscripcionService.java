package com.educativa.intranet.service.asistencia;

import com.educativa.intranet.dto.AsistenciaDiariaDTO;

public interface IAsistenciaInscripcionService {
    void registrarAsistenciaDiaria(AsistenciaDiariaDTO dto, Long auxiliarUsuarioId);
    void corregirAsistencia(Long asistenciaId, String nuevoEstado, Long auxiliarUsuarioId);
}
