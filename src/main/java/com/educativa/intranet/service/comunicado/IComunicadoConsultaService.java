package com.educativa.intranet.service.comunicado;

import com.educativa.intranet.dto.comunicado.ComunicadoResponseDTO;
import java.util.List;

public interface IComunicadoConsultaService {
    List<ComunicadoResponseDTO> obtenerMiMuroDeNoticias(Long usuarioLogueadoId);
}
