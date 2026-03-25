package com.educativa.intranet.controller;

import com.educativa.intranet.dto.MatriculaMasivaDTO;
import com.educativa.intranet.dto.MatriculaResponseDTO;
import com.educativa.intranet.service.academico.IMatriculaConsultaService;
import com.educativa.intranet.service.academico.IMatriculaInscripcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/academico")
@RequiredArgsConstructor
public class AcademicoController {

    private final IMatriculaInscripcionService inscripcionService;
    private final IMatriculaConsultaService consultaService;

    // Acción masiva: Se le manda un ID de Curso y un Array [1, 5, 8] de IDs de Alumnos
    @PostMapping("/matricular")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MatriculaResponseDTO>> matricularMasivo(@Valid @RequestBody MatriculaMasivaDTO dto) {
        return ResponseEntity.ok(inscripcionService.matricularAlumnosMasivamente(dto));
    }

    // Para ver quién está en el salón
    @GetMapping("/curso/{cursoId}/alumnos")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
    public ResponseEntity<List<MatriculaResponseDTO>> obtenerAlumnosDelCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(consultaService.verListaDeSalon(cursoId));
    }
}
