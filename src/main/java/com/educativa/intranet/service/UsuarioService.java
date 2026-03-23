package com.educativa.intranet.service;

import com.educativa.intranet.dto.PasswordResetDTO;
import com.educativa.intranet.dto.UsuarioCreateDTO;
import com.educativa.intranet.dto.UsuarioResponseDTO;
import com.educativa.intranet.model.Usuario;
import com.educativa.intranet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioCreateDTO dto) {
        // Regla: No se pueden repetir emails en el colegio.
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado en el colegio.");
        }

        Usuario nuevoUsuario = Usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .rol(dto.getRol())
                .activo(true) // Todos nacen con acceso habilitado
                .build();

        usuarioRepository.save(nuevoUsuario);

        return mapearADTO(nuevoUsuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    // APLICACIÓN DEL BLOQUEO LÓGICO
    @Transactional
    public void cambiarEstado(Long id, boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario inexistente."));
        
        // Literalmente le bajamos el switch. El JWT lo rechazará automáticamente a partir de hoy.
        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }

    // EL DIRECTOR RESETEA CONTRASEÑAS SIN PREGUNTAR LA ANTERIOR
    @Transactional
    public void resetearClave(Long id, PasswordResetDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario inexistente."));

        // Se reescribe la contraseña pero pasando por la trituradora de hash (BCrypt).
        usuario.setPassword(passwordEncoder.encode(dto.getNuevaPassword()));
        usuarioRepository.save(usuario);
    }

    // Centralizamos la fábrica de Cajas de Cartón
    private UsuarioResponseDTO mapearADTO(Usuario user) {
        return UsuarioResponseDTO.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .email(user.getEmail())
                .rol(user.getRol().name())
                .activo(user.getActivo() != null ? user.getActivo() : true)
                .build();
    }
}
