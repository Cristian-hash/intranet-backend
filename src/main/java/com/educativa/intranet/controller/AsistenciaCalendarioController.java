package com.educativa.intranet.controller;

import com.educativa.intranet.dto.AsistenciaCalendarioDTO;
import com.educativa.intranet.security.UserPrincipal;
import com.educativa.intranet.service.AsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/alumno/asistencias")
@RequiredArgsConstructor
public class AsistenciaCalendarioController {

    private final AsistenciaService asistenciaService;

    // Cuando WordPress quiera pintar el calendario, llamará aquí:
    // GET: /alumno/asistencias/calendario?anio=2026&mes=2
    @GetMapping("/calendario")
    @PreAuthorize("hasAnyRole('ALUMNO', 'PADRE')")
    public ResponseEntity<List<AsistenciaCalendarioDTO>> verMiCalendario(
            @RequestParam int anio,
            @RequestParam int mes,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
            
        // Usamos el ID de JWT del alumno/padre para que nadie vea las faltas del otro
        List<AsistenciaCalendarioDTO> calendario = asistenciaService.obtenerCalendarioMensual(userPrincipal.getId(), anio, mes);
        return ResponseEntity.ok(calendario);
    }
}
