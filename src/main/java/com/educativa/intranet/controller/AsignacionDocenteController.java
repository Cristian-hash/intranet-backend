package com.educativa.intranet.controller;

import com.educativa.intranet.dto.AsignacionDocenteCreateDTO;
import com.educativa.intranet.dto.AsignacionDocenteResponseDTO;
import com.educativa.intranet.service.asignacion.IAsignacionDocenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/asignaciones")
@RequiredArgsConstructor
public class AsignacionDocenteController {

    private final IAsignacionDocenteService asignacionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AsignacionDocenteResponseDTO> crearAsignacion(
            @Valid @RequestBody AsignacionDocenteCreateDTO dto) {
        return ResponseEntity.ok(asignacionService.crearAsignacion(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AsignacionDocenteResponseDTO>> listarTodas() {
        return ResponseEntity.ok(asignacionService.listarTodas());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> desactivarAsignacion(@PathVariable Long id) {
        asignacionService.desactivarAsignacion(id);
        return ResponseEntity.ok("Asignación desactivada exitosamente.");
    }
}
