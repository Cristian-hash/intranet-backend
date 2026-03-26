package com.educativa.intranet.service.comunicado;

import com.educativa.intranet.dto.comunicado.ComunicadoResponseDTO;
import com.educativa.intranet.model.Comunicado;
import com.educativa.intranet.model.Rol;
import com.educativa.intranet.model.TipoAudiencia;
import com.educativa.intranet.model.Usuario;
import com.educativa.intranet.repository.ComunicadoRepository;
import com.educativa.intranet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComunicadoConsultaServiceImpl implements IComunicadoConsultaService {

    private final ComunicadoRepository comunicadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComunicadoMapper comunicadoMapper; // SRP: el mapeo le pertenece al experto

    @Override
    @Transactional(readOnly = true)
    public List<ComunicadoResponseDTO> obtenerMiMuroDeNoticias(Long usuarioLogueadoId) {
        Usuario usuario = usuarioRepository.findById(usuarioLogueadoId).orElseThrow();

        // El Cerebro deduce matemáticamente qué enum de audiencia eres según tu Rol
        TipoAudiencia miAudiencia = mapearRolAAudiencia(usuario.getRol());

        List<Comunicado> comunicadosBrutos = comunicadoRepository
                .findByAudienciaOrGlobalOrderByFechaDesc(miAudiencia);

        // Pipeline limpio: la construcción del DTO la delega el Mapper
        return comunicadosBrutos.stream()
                .map(c -> comunicadoMapper.convertirHaciaDTO(c, usuarioLogueadoId))
                .collect(Collectors.toList());
    }

    // OCP: Si añadimos el Rol COORDINADOR, solo añadimos un 'if' aquí, en su lugar correcto
    private TipoAudiencia mapearRolAAudiencia(Rol rol) {
        if (rol == Rol.ALUMNO)   return TipoAudiencia.SOLO_ALUMNOS;
        if (rol == Rol.PADRE)    return TipoAudiencia.SOLO_PADRES;
        if (rol == Rol.PROFESOR) return TipoAudiencia.SOLO_PROFESORES;
        return TipoAudiencia.GLOBAL;
    }
}
