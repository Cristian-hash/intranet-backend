package com.educativa.intranet.controller;

import com.educativa.intranet.dto.PeriodoCreateDTO;
import com.educativa.intranet.dto.PeriodoResponseDTO;
import com.educativa.intranet.service.PeriodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/periodos")
@RequiredArgsConstructor
public class PeriodoController {

    private final PeriodoService periodoService;

    @PostMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PeriodoResponseDTO> crearBimestre(@Valid @RequestBody PeriodoCreateDTO dto) {
        return ResponseEntity.ok(periodoService.crearPeriodo(dto));
    }

    @GetMapping("/listar")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')") // El profesor necesita leerlos para saber dónde inyectar notas
    public ResponseEntity<List<PeriodoResponseDTO>> listarBimestres() {
        return ResponseEntity.ok(periodoService.listarTodos());
    }

    @PutMapping("/{id}/cerrar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> cerrarBimestre(@PathVariable Long id) {
        periodoService.cambiarEstadoPeriodo(id, false);
        return ResponseEntity.ok("Periodo cerrado. Ningún profesor podrá subir notas nuevas aquí.");
    }
    
    @PutMapping("/{id}/abrir")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> reabrirBimestre(@PathVariable Long id) {
        periodoService.cambiarEstadoPeriodo(id, true);
        return ResponseEntity.ok("Periodo abierto exitosamente.");
    }
}
