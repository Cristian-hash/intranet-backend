package com.educativa.intranet.service.comunicado;

import com.educativa.intranet.dto.comunicado.ComunicadoResponseDTO;
import com.educativa.intranet.model.Comunicado;
import com.educativa.intranet.repository.LecturaComunicadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ComunicadoMapper {

    private final LecturaComunicadoRepository lecturaRepository;

    /**
     * Convierte un Comunicado de BD a un DTO de respuesta.
     * Necesita el ID del lector para calcular el campo "yaLoLei" de manera limpia.
     */
    public ComunicadoResponseDTO convertirHaciaDTO(Comunicado comunicado, Long lectorId) {
        boolean loLeyo = lecturaRepository.existsByComunicadoIdAndLectorId(comunicado.getId(), lectorId);
        return ComunicadoResponseDTO.builder()
                .id(comunicado.getId())
                .titulo(comunicado.getTitulo())
                .contenido(comunicado.getContenido())
                .autor(comunicado.getAutor().getNombre())
                .fechaPublicacion(comunicado.getFechaPublicacion())
                .yaLoLei(loLeyo)
                .build();
    }
}
