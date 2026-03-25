package com.educativa.intranet.service.asistencia;

import com.educativa.intranet.dto.AsistenciaCalendarioDTO;
import com.educativa.intranet.repository.AsistenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsistenciaConsultaServiceImpl implements IAsistenciaConsultaService {

    private final AsistenciaRepository asistenciaRepository;
    private final AsistenciaMapper asistenciaMapper; // Delegamos la creación del DTO a su experto (SRP)

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaCalendarioDTO> obtenerCalendarioMensual(Long usuarioId, int anio, int mes) {
        // Le pedimos al experto en bases de datos que extraiga las fechas exactas
        return asistenciaRepository.findByUsuarioAndMesExacto(usuarioId, anio, mes)
                .stream()
                .map(asistenciaMapper::convertirHaciaDTO) // Pipeline muy limpio con uso de Metodos de Referencia
                .collect(Collectors.toList());
    }
}
