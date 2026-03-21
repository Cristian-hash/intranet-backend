package com.educativa.intranet.controller;

import com.educativa.intranet.dto.JwtAuthResponse;
import com.educativa.intranet.dto.LoginRequest;
import com.educativa.intranet.model.LogAcceso;
import com.educativa.intranet.model.Usuario;
import com.educativa.intranet.repository.LogAccesoRepository;
import com.educativa.intranet.repository.UsuarioRepository;
import com.educativa.intranet.security.JwtTokenProvider;
import com.educativa.intranet.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;
    private final LogAccesoRepository logAccesoRepository;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findById(userPrincipal.getId()).orElseThrow();

        // AUDITORÍA: Guardar Log de Acceso
        String ipAddress = request.getHeader("X-FORWARDED-FOR");  
        if (ipAddress == null) {  
            ipAddress = request.getRemoteAddr();  
        }
        
        LogAcceso log = LogAcceso.builder()
                .usuario(usuario)
                .fecha(LocalDateTime.now())
                .ip(ipAddress)
                .build();
        logAccesoRepository.save(log);

        return ResponseEntity.ok(new JwtAuthResponse(jwt, usuario.getRol().name(), usuario.getNombre()));
    }
}
