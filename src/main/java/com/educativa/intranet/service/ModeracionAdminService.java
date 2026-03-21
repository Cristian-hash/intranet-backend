package com.educativa.intranet.service;

import com.educativa.intranet.dto.AlertaResponseDTO;
import com.educativa.intranet.model.AlertaModeracion;
import com.educativa.intranet.repository.AlertaModeracionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModeracionAdminService {

    private final AlertaModeracionRepository alertaRepository;

    @Transactional(readOnly = true)
    public List<AlertaResponseDTO> obtenerAlertasPendientes() {
        // El guardia busca en el sótano todas las banderas rojas que nadie haya revisado aún
        return alertaRepository.findByRevisadoPorAdminFalse().stream()
                .map(alerta -> AlertaResponseDTO.builder()
                        .alertaId(alerta.getId())
                        .emisorNombre(alerta.getMensaje().getEmisor().getNombre())
                        .receptorNombre(alerta.getMensaje().getReceptor().getNombre())
                        .contenidoMensaje(alerta.getMensaje().getContenido())
                        .categoriaRiesgo(alerta.getCategoria().name())
                        .detalleAnalisis(alerta.getDetalleAnalisis())
                        .fechaDeteccion(alerta.getFechaDeteccion())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void marcarComoRevisada(Long alertaId) {
        AlertaModeracion alerta = alertaRepository.findById(alertaId)
                .orElseThrow(() -> new RuntimeException("No se encontró la Alerta ID: " + alertaId));
        
        alerta.setRevisadoPorAdmin(true);
        alertaRepository.save(alerta); // El Director apaga la sirena mandando a guardar la actualización.
    }
}
