package com.educativa.intranet.service;

import com.educativa.intranet.dto.NotaResponseDTO;
import com.educativa.intranet.model.Alumno;
import com.educativa.intranet.model.Usuario;
import com.educativa.intranet.repository.NotaRepository;
import com.educativa.intranet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlumnoService {
    
    private final NotaRepository notaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<NotaResponseDTO> obtenerMisNotas(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario autenticado inválido"));
            
        Alumno alumno = usuario.getAlumno();
        if(alumno == null) {
            throw new RuntimeException("No tiene el perfil activo como estudiante");
        }

        return notaRepository.findByAlumnoId(alumno.getId()).stream()
                .map(nota -> NotaResponseDTO.builder()
                        .id(nota.getId())
                        .curso(nota.getCurso().getNombre())
                        .profesor(nota.getProfesor().getUsuario().getNombre())
                        .valor(nota.getValor())
                        .build())
                .collect(Collectors.toList());
    }
}
