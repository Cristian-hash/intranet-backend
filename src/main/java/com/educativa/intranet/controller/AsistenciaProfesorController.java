package com.educativa.intranet.controller;

import com.educativa.intranet.dto.AsistenciaMasivaDTO;
import com.educativa.intranet.service.asistencia.IAsistenciaInscripcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profesor/asistencias")
@RequiredArgsConstructor
public class AsistenciaProfesorController {

    private final IAsistenciaInscripcionService inscripcionService;

    @PostMapping("/lote")
    @PreAuthorize("hasRole('PROFESOR')") // Seguridad Extrema: Solo los profesores pueden calificar esto
    public ResponseEntity<String> registrarAsistenciaDelDia(@Valid @RequestBody AsistenciaMasivaDTO dto) {
        inscripcionService.registrarAsistenciaVeloz(dto);
        return ResponseEntity.ok("Se han registrado las asistencias de todo el salón correctamente.");
    }
}
