package com.educativa.intranet.controller;

import com.educativa.intranet.dto.NotaCreateDTO;
import com.educativa.intranet.security.UserPrincipal;
import com.educativa.intranet.service.ProfesorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/profesor")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService profesorService;

    @PostMapping("/notas")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<?> registrarNota(
            @Valid @RequestBody NotaCreateDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
            
        profesorService.registrarNota(userPrincipal.getId(), dto);
        return ResponseEntity.ok(Map.of("mensaje", "Nota registrada exitosamente en el sistema"));
    }
}
