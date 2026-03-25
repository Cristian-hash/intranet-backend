package com.educativa.intranet.service.perfil;

import com.educativa.intranet.dto.ProfesorPerfilCreateDTO;
import com.educativa.intranet.dto.ProfesorPerfilResponseDTO;
import java.util.List;

public interface IProfesorPerfilService {
    ProfesorPerfilResponseDTO construirPerfilProfesor(ProfesorPerfilCreateDTO dto);
    List<ProfesorPerfilResponseDTO> listarTodosLosProfesoresCompletos();
}
