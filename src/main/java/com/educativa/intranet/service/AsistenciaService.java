package com.educativa.intranet.service;

import com.educativa.intranet.dto.AsistenciaCalendarioDTO;
import com.educativa.intranet.repository.AsistenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;

    @Transactional(readOnly = true)
    public List<AsistenciaCalendarioDTO> obtenerCalendarioMensual(Long usuarioId, int anio, int mes) {
        // Le pedimos al guardia todas las anotaciones de Febrero 2026
        return asistenciaRepository.findByUsuarioAndMesExacto(usuarioId, anio, mes)
                .stream()
                .map(asist -> AsistenciaCalendarioDTO.builder()
                        .fecha(asist.getFecha())
                        .dia(asist.getFecha().getDayOfMonth()) // Extraemos el número 5
                        .estado(asist.getEstado().name()) // PRESENTE o AUSENTE
                        .build())
                .collect(Collectors.toList());
    }
}
