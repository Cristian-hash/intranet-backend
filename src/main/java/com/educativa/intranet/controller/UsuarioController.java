package com.educativa.intranet.controller;

import com.educativa.intranet.dto.PasswordResetDTO;
import com.educativa.intranet.dto.UsuarioCreateDTO;
import com.educativa.intranet.dto.UsuarioResponseDTO;
import com.educativa.intranet.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // SOLO LA DIRECTIVA ENTRA AQUÍ
    
    @PostMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> crearCuentaBase(@Valid @RequestBody UsuarioCreateDTO dto) {
        return ResponseEntity.ok(usuarioService.crearUsuario(dto));
    }

    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDTO>> verTodoElPersonal() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // Bloquea temporalmente a un Alumno expulsado o a un Profesor cesado
    @PutMapping("/{id}/bloquear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> bloquearAcceeso(@PathVariable Long id) {
        usuarioService.cambiarEstado(id, false);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario bloqueado y despojado de accesos."));
    }

    // Levanta el castigo
    @PutMapping("/{id}/desbloquear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> desbloquearUsuario(@PathVariable Long id) {
        usuarioService.cambiarEstado(id, true);
        return ResponseEntity.ok(Map.of("mensaje", "Acceso restaurado exitosamente."));
    }

    // Cuando el alumno se olvida la contraseña
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> forzarClaveNueva(
            @PathVariable Long id, 
            @Valid @RequestBody PasswordResetDTO dto) {
            
        usuarioService.resetearClave(id, dto);
        return ResponseEntity.ok(Map.of("mensaje", "Contraseña reescrita. El usuario ya puede iniciar sesión con ella."));
    }
}
