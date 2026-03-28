package com.educativa.intranet.service.perfil;

import com.educativa.intranet.dto.AlumnoPerfilCreateDTO;
import com.educativa.intranet.dto.AlumnoPerfilResponseDTO;
import java.util.List;

public interface IAlumnoPerfilService {
    AlumnoPerfilResponseDTO construirPerfilAlumno(AlumnoPerfilCreateDTO dto);
    List<AlumnoPerfilResponseDTO> listarTodosLosAlumnosCompletos();
    List<AlumnoPerfilResponseDTO> listarPorGradoYSeccion(String grado, String seccion);
}
