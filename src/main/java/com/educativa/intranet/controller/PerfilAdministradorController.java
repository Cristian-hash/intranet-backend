package com.educativa.intranet.controller;

import com.educativa.intranet.dto.AlumnoPerfilCreateDTO;
import com.educativa.intranet.dto.AlumnoPerfilResponseDTO;
import com.educativa.intranet.service.PerfilAdministradorService;
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

    private final PerfilAdministradorService perfilService;

    // La secretaria o el Director armando un perfil
    @PostMapping("/alumno")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlumnoPerfilResponseDTO> estructurarPerfilAlumno(@Valid @RequestBody AlumnoPerfilCreateDTO dto) {
        return ResponseEntity.ok(perfilService.construirPerfilAlumno(dto));
    }

    @GetMapping("/alumno/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AlumnoPerfilResponseDTO>> consultarCuadroCompleto() {
        return ResponseEntity.ok(perfilService.listarTodosLosAlumnosCompletos());
    }
}
