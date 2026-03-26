package com.educativa.intranet.service.comunicado;

import com.educativa.intranet.dto.comunicado.ComunicadoCreateDTO;

public interface IComunicadoPublicacionService {
    Long redactarComunicado(Long autorId, ComunicadoCreateDTO dto);
}
