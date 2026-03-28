package com.educativa.intranet.service.asignacion;

import com.educativa.intranet.dto.AsignacionDocenteCreateDTO;
import com.educativa.intranet.dto.AsignacionDocenteResponseDTO;

import java.util.List;

public interface IAsignacionDocenteService {
    AsignacionDocenteResponseDTO crearAsignacion(AsignacionDocenteCreateDTO dto);
    List<AsignacionDocenteResponseDTO> listarTodas();
    List<AsignacionDocenteResponseDTO> listarMisCursos(Long profesorId);
    void desactivarAsignacion(Long asignacionId);
}
