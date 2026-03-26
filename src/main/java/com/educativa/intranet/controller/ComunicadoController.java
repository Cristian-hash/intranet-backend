package com.educativa.intranet.controller;

import com.educativa.intranet.dto.comunicado.ComunicadoCreateDTO;
import com.educativa.intranet.dto.comunicado.ComunicadoResponseDTO;
import com.educativa.intranet.security.UserPrincipal;
import com.educativa.intranet.service.ComunicadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/muro")
@RequiredArgsConstructor
@Tag(name = "Muro de Comunicados", description = "API para gestionar los anuncios y comunicados de la institución")
@SecurityRequirement(name = "bearerAuth")
public class ComunicadoController {

    private final ComunicadoService comunicadoService;

    // Solo el dueño de la escuela publica
    @PostMapping("/publicar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Publicar Anuncio", description = "Permite a un ADMIN redactar y publicar un comunicado dirigido a TODA la escuela, un GRADO específico o un CURSO concreto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Megáfono: Anuncio propagado con éxito."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en el formulario."),
            @ApiResponse(responseCode = "403", description = "No Autorizado o token inválido (Requiere rol ADMIN).")
    })
    public ResponseEntity<String> publicarAnuncio(
            @AuthenticationPrincipal UserPrincipal admin,
            @Valid @RequestBody ComunicadoCreateDTO dto) {
        comunicadoService.redactarComunicado(admin.getId(), dto);
        return ResponseEntity.ok("Megáfono: Anuncio propagado con éxito.");
    }

    // El Alumno, Padre o Profesor abre su celular y pide SUS noticias correspondientes
    @GetMapping("/mis-noticias")
    @Operation(summary = "Ver mi muro de noticias", description = "Obtiene la lista de comunicados relevantes para el usuario autenticado (Alumno, Padre, Profesor, etc.).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de noticias obtenida exitosamente."),
            @ApiResponse(responseCode = "403", description = "Token no proporcionado o inválido.")
    })
    public ResponseEntity<List<ComunicadoResponseDTO>> leerMuro(@AuthenticationPrincipal UserPrincipal usuarioLogueado) {
        return ResponseEntity.ok(comunicadoService.obtenerMiMuroDeNoticias(usuarioLogueado.getId()));
    }

    // El botón de "Entendido" en la interfaz de Angular
    @PostMapping("/{id}/confirmar-lectura")
    @Operation(summary = "Confirmar lectura", description = "Marca un comunicado como leído ('enterado') por el usuario actual.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Firma electrónica de enterado guardada."),
            @ApiResponse(responseCode = "403", description = "Token no proporcionado o inválido."),
            @ApiResponse(responseCode = "404", description = "Comunicado no encontrado.")
    })
    public ResponseEntity<String> reportarLectura(
            @AuthenticationPrincipal UserPrincipal usuarioLogueado,
            @PathVariable Long id) {
        comunicadoService.firmarDeEnterado(usuarioLogueado.getId(), id);
        return ResponseEntity.ok("Firma electrónica de enterado guardada.");
    }
}
