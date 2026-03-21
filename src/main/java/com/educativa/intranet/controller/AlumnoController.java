package com.educativa.intranet.controller;

import com.educativa.intranet.dto.NotaResponseDTO;
import com.educativa.intranet.security.UserPrincipal;
import com.educativa.intranet.service.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/alumno")
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoService alumnoService;

    @GetMapping("/notas")
    @PreAuthorize("hasRole('ALUMNO')")
    public ResponseEntity<List<NotaResponseDTO>> misNotas(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<NotaResponseDTO> notas = alumnoService.obtenerMisNotas(userPrincipal.getId());
        return ResponseEntity.ok(notas);
    }
}
