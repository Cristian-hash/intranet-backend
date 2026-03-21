package com.educativa.intranet.controller;

import com.educativa.intranet.dto.AlertaResponseDTO;
import com.educativa.intranet.service.ModeracionAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/moderacion")
@RequiredArgsConstructor
public class ModeracionAdminController {

    private final ModeracionAdminService moderacionAdminService;

    // Solo los Directores o usuarios etiquetados como ADMIN pasarán la puerta de JWT
    @GetMapping("/alertas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AlertaResponseDTO>> listarAlertasPeligrosas() {
        List<AlertaResponseDTO> alertas = moderacionAdminService.obtenerAlertasPendientes();
        return ResponseEntity.ok(alertas);
    }

    // Ruta para apagar las sirenas (El Falso Positivo fue revisado o el caso fue resuelto)
    @PutMapping("/alertas/{id}/revisar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resolverProblemaDeChat(@PathVariable Long id) {
        moderacionAdminService.marcarComoRevisada(id);
        return ResponseEntity.ok(Map.of("mensaje", "La alerta ha sido clasificada y archivada exitosamente."));
    }
}
