package com.educativa.intranet.service;

import com.educativa.intranet.dto.comunicado.ComunicadoCreateDTO;
import com.educativa.intranet.dto.comunicado.ComunicadoResponseDTO;
import com.educativa.intranet.model.*;
import com.educativa.intranet.repository.ComunicadoRepository;
import com.educativa.intranet.repository.LecturaComunicadoRepository;
import com.educativa.intranet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComunicadoService {

    private final ComunicadoRepository comunicadoRepository;
    private final LecturaComunicadoRepository lecturaRepository;
    private final UsuarioRepository usuarioRepository;

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

    @Transactional(readOnly = true)
    public List<ComunicadoResponseDTO> obtenerMiMuroDeNoticias(Long usuarioLogueadoId) {
        Usuario usuario = usuarioRepository.findById(usuarioLogueadoId).orElseThrow();
        
        // El Cerebro deduce matemáticamente qué enum de audiencia eres según tu Rol de Spring
        TipoAudiencia miAudiencia = mapearRolAAudiencia(usuario.getRol());

        List<Comunicado> comunicadosBrutos = comunicadoRepository.findByAudienciaOrGlobalOrderByFechaDesc(miAudiencia);

        return comunicadosBrutos.stream().map(c -> {
            boolean loLeyo = lecturaRepository.existsByComunicadoIdAndLectorId(c.getId(), usuarioLogueadoId);
            return ComunicadoResponseDTO.builder()
                    .id(c.getId())
                    .titulo(c.getTitulo())
                    .contenido(c.getContenido())
                    .autor(c.getAutor().getNombre())
                    .fechaPublicacion(c.getFechaPublicacion())
                    .yaLoLei(loLeyo)
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional
    public void firmarDeEnterado(Long usuarioId, Long comunicadoId) {
        boolean yaFirmado = lecturaRepository.existsByComunicadoIdAndLectorId(comunicadoId, usuarioId);
        if (yaFirmado) return; // Evitamos duplicar firmas en BD

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

    private TipoAudiencia mapearRolAAudiencia(Rol rol) {
        if (rol == Rol.ALUMNO) return TipoAudiencia.SOLO_ALUMNOS;
        if (rol == Rol.PADRE) return TipoAudiencia.SOLO_PADRES;
        if (rol == Rol.PROFESOR) return TipoAudiencia.SOLO_PROFESORES;
        return TipoAudiencia.GLOBAL;
    }
}
