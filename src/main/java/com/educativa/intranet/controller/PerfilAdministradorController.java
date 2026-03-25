package com.educativa.intranet.controller;

import com.educativa.intranet.dto.*;
import com.educativa.intranet.service.perfil.IAlumnoPerfilService;
import com.educativa.intranet.service.perfil.IPadrePerfilService;
import com.educativa.intranet.service.perfil.IProfesorPerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/perfiles")
@RequiredArgsConstructor
public class PerfilAdministradorController {

    private final IAlumnoPerfilService alumnoService;
    private final IProfesorPerfilService profesorService;
    private final IPadrePerfilService padreService;

    // --- ALUMNOS ---
    @PostMapping("/alumno")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlumnoPerfilResponseDTO> estructurarPerfilAlumno(@Valid @RequestBody AlumnoPerfilCreateDTO dto) {
        return ResponseEntity.ok(alumnoService.construirPerfilAlumno(dto));
    }

    @GetMapping("/alumno/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AlumnoPerfilResponseDTO>> consultarCuadroAlumnos() {
        return ResponseEntity.ok(alumnoService.listarTodosLosAlumnosCompletos());
    }

    // --- PROFESORES ---
    @PostMapping("/profesor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfesorPerfilResponseDTO> estructurarPerfilProfesor(@Valid @RequestBody ProfesorPerfilCreateDTO dto) {
        return ResponseEntity.ok(profesorService.construirPerfilProfesor(dto));
    }

    @GetMapping("/profesor/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProfesorPerfilResponseDTO>> consultarCuadroProfesores() {
        return ResponseEntity.ok(profesorService.listarTodosLosProfesoresCompletos());
    }

    // --- PADRES ---
    @PostMapping("/padre")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PadrePerfilResponseDTO> estructurarPerfilPadre(@Valid @RequestBody PadrePerfilCreateDTO dto) {
        return ResponseEntity.ok(padreService.construirPerfilPadre(dto));
    }

    @GetMapping("/padre/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PadrePerfilResponseDTO>> consultarCuadroPadres() {
        return ResponseEntity.ok(padreService.listarTodosLosPadresCompletos());
    }
}
