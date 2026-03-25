package com.educativa.intranet.service.perfil;

import com.educativa.intranet.dto.PadrePerfilCreateDTO;
import com.educativa.intranet.dto.PadrePerfilResponseDTO;
import java.util.List;

public interface IPadrePerfilService {
    PadrePerfilResponseDTO construirPerfilPadre(PadrePerfilCreateDTO dto);
    List<PadrePerfilResponseDTO> listarTodosLosPadresCompletos();
}
