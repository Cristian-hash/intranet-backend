package com.educativa.intranet.controller;

import com.educativa.intranet.dto.AsistenciaDiariaDTO;
import com.educativa.intranet.security.UserPrincipal;
import com.educativa.intranet.service.asistencia.IAsistenciaInscripcionService;
import com.educativa.intranet.service.perfil.IAlumnoPerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auxiliar/asistencias")
@RequiredArgsConstructor
public class AsistenciaProfesorController {

    private final IAsistenciaInscripcionService inscripcionService;
    private final IAlumnoPerfilService perfilService;

    @PostMapping("/diaria")
    @PreAuthorize("hasRole('AUXILIAR')")
    public ResponseEntity<String> registrarAsistenciaDelDia(
            @Valid @RequestBody AsistenciaDiariaDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        inscripcionService.registrarAsistenciaDiaria(dto, userPrincipal.getId());
        return ResponseEntity.ok("Se ha registrado la asistencia del día correctamente.");
    }

    @PutMapping("/{id}/corregir")
    @PreAuthorize("hasRole('AUXILIAR')")
    public ResponseEntity<String> corregirAsistencia(
            @PathVariable Long id,
            @RequestParam String nuevoEstado,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        inscripcionService.corregirAsistencia(id, nuevoEstado, userPrincipal.getId());
        return ResponseEntity.ok("Asistencia corregida correctamente.");
    }

    @GetMapping("/alumnos")
    @PreAuthorize("hasRole('AUXILIAR')")
    public ResponseEntity<?> obtenerAlumnosPorSalon(
            @RequestParam String grado,
            @RequestParam String seccion) {
        // En un refactor futuro, esto sería mejor inyectarlo vía un facade.
        // Por ahora delegamos directamente al servicio que tiene la lógica:
        return ResponseEntity.ok(perfilService.listarPorGradoYSeccion(grado, seccion));
    }
}
