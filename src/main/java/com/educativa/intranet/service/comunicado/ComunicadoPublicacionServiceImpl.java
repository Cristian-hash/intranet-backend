package com.educativa.intranet.service.comunicado;

import com.educativa.intranet.dto.comunicado.ComunicadoCreateDTO;
import com.educativa.intranet.model.Comunicado;
import com.educativa.intranet.model.Usuario;
import com.educativa.intranet.repository.ComunicadoRepository;
import com.educativa.intranet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ComunicadoPublicacionServiceImpl implements IComunicadoPublicacionService {

    private final ComunicadoRepository comunicadoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public Long redactarComunicado(Long autorId, ComunicadoCreateDTO dto) {
        Usuario adminLogueado = usuarioRepository.findById(autorId)
                .orElseThrow(() -> new RuntimeException("Autor no válido"));

        Comunicado nuevo = Comunicado.builder()
                .titulo(dto.getTitulo())
                .contenido(dto.getContenido())
                .fechaPublicacion(LocalDateTime.now())
                .audienciaDestino(dto.getAudienciaDestino())
                .gradoDestino(dto.getGradoDestino())
                .autor(adminLogueado)
                .build();

        return comunicadoRepository.save(nuevo).getId();
    }
}
