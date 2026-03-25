package com.educativa.intranet.service;

import com.educativa.intranet.dto.PeriodoCreateDTO;
import com.educativa.intranet.dto.PeriodoResponseDTO;
import com.educativa.intranet.model.Periodo;
import com.educativa.intranet.repository.PeriodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PeriodoService {

    private final PeriodoRepository periodoRepository;

    @Transactional
    public PeriodoResponseDTO crearPeriodo(PeriodoCreateDTO dto) {
        validarFechasTenganSentido(dto);
        validarCruceDeNombres(dto.getNombre());

        Periodo nuevoPeriodo = asentarNuevoPeriodo(dto);
        return convertirHaciaDTO(nuevoPeriodo);
    }

    @Transactional(readOnly = true)
    public List<PeriodoResponseDTO> listarTodos() {
        return periodoRepository.findAll().stream()
                .map(this::convertirHaciaDTO)
                .collect(Collectors.toList());
    }

    // Cerrar el periodo bloquea la tabla de Notas automáticamente para el futuro
    @Transactional
    public void cambiarEstadoPeriodo(Long id, boolean estado) {
        Periodo periodo = obtenerPeriodoOArrojarError(id);
        periodo.setActivo(estado);
        periodoRepository.save(periodo);
    }

    // =====================================
    // FUNCIONES PRIVADAS (CLEAN CODE)
    // =====================================

    private void validarFechasTenganSentido(PeriodoCreateDTO dto) {
        if (dto.getFechaFin().isBefore(dto.getFechaInicio())) {
            throw new RuntimeException("Regla Temporal: La fecha de fin no puede ser ANTES de la fecha de inicio.");
        }
    }

    private void validarCruceDeNombres(String nombre) {
        if (periodoRepository.findByNombre(nombre).isPresent()) {
            throw new RuntimeException("Regla Académica: El periodo con nombre '" + nombre + "' ya existe.");
        }
    }

    private Periodo obtenerPeriodoOArrojarError(Long id) {
        return periodoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El Periodo solicitado no existe."));
    }

    private Periodo asentarNuevoPeriodo(PeriodoCreateDTO dto) {
        Periodo p = Periodo.builder()
                .nombre(dto.getNombre())
                .fechaInicio(dto.getFechaInicio())
                .fechaFin(dto.getFechaFin())
                .activo(true)
                .build();
        return periodoRepository.save(p);
    }

    private PeriodoResponseDTO convertirHaciaDTO(Periodo p) {
        return PeriodoResponseDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .fechaInicio(p.getFechaInicio())
                .fechaFin(p.getFechaFin())
                .activo(p.getActivo())
                .build();
    }
}
