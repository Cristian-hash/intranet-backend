package com.educativa.intranet.service.comunicado;

import com.educativa.intranet.model.Comunicado;
import com.educativa.intranet.model.LecturaComunicado;
import com.educativa.intranet.model.Usuario;
import com.educativa.intranet.repository.ComunicadoRepository;
import com.educativa.intranet.repository.LecturaComunicadoRepository;
import com.educativa.intranet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ComunicadoLecturaServiceImpl implements IComunicadoLecturaService {

    private final LecturaComunicadoRepository lecturaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComunicadoRepository comunicadoRepository;

    @Override
    @Transactional
    public void firmarDeEnterado(Long usuarioId, Long comunicadoId) {
        // Guard clause (Clean Code): Salimos rápido si ya está firmado. Evita anidamiento innecesario.
        if (lecturaRepository.existsByComunicadoIdAndLectorId(comunicadoId, usuarioId)) return;

        Usuario lector = usuarioRepository.findById(usuarioId).orElseThrow();
        Comunicado comunicado = comunicadoRepository.findById(comunicadoId)
                .orElseThrow(() -> new RuntimeException("El comunicado no existe"));

        LecturaComunicado firma = LecturaComunicado.builder()
                .comunicado(comunicado)
                .lector(lector)
                .fechaLectura(LocalDateTime.now())
                .build();

        lecturaRepository.save(firma);
    }
}
