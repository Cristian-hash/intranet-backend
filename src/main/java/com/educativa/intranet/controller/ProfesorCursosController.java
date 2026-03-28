package com.educativa.intranet.controller;

import com.educativa.intranet.dto.AsignacionDocenteResponseDTO;
import com.educativa.intranet.security.UserPrincipal;
import com.educativa.intranet.service.asignacion.IAsignacionDocenteService;
import com.educativa.intranet.model.Profesor;
import com.educativa.intranet.model.Usuario;
import com.educativa.intranet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesor")
@RequiredArgsConstructor
public class ProfesorCursosController {

    private final IAsignacionDocenteService asignacionService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/mis-cursos")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<List<AsignacionDocenteResponseDTO>> misCursos(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Usuario usuario = usuarioRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Profesor profesor = usuario.getProfesor();
        if (profesor == null) {
            throw new RuntimeException("El usuario no tiene perfil de profesor");
        }

        return ResponseEntity.ok(asignacionService.listarMisCursos(profesor.getId()));
    }
}
