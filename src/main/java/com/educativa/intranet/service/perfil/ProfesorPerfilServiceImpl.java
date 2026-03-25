package com.educativa.intranet.service.perfil;

import com.educativa.intranet.dto.ProfesorPerfilCreateDTO;
import com.educativa.intranet.dto.ProfesorPerfilResponseDTO;
import com.educativa.intranet.model.Profesor;
import com.educativa.intranet.model.Rol;
import com.educativa.intranet.model.Usuario;
import com.educativa.intranet.repository.ProfesorRepository;
import com.educativa.intranet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfesorPerfilServiceImpl implements IProfesorPerfilService {

    private final UsuarioRepository usuarioRepository;
    private final ProfesorRepository profesorRepository;

    @Override
    @Transactional
    public ProfesorPerfilResponseDTO construirPerfilProfesor(ProfesorPerfilCreateDTO dto) {
        Usuario usuario = buscarYValidarUsuario(dto.getUsuarioId(), Rol.PROFESOR);

        Profesor profesorFisico = Profesor.builder()
                .usuario(usuario)
                .especialidad(dto.getEspecialidad())
                .build();

        profesorRepository.save(profesorFisico);
        return mapearADTO(profesorFisico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfesorPerfilResponseDTO> listarTodosLosProfesoresCompletos() {
        return profesorRepository.findAll().stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    private Usuario buscarYValidarUsuario(Long usuarioId, Rol rolEsperado) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Ese usuario matriz no existe."));

        if (!usuario.getRol().equals(rolEsperado)) {
            throw new RuntimeException("Regla de RRHH: Intentas crear un perfil de " + rolEsperado + " sobre un usuario que es " + usuario.getRol());
        }
        return usuario;
    }

    private ProfesorPerfilResponseDTO mapearADTO(Profesor prof) {
        return ProfesorPerfilResponseDTO.builder()
                .perfilId(prof.getId())
                .usuarioId(prof.getUsuario().getId())
                .nombre(prof.getUsuario().getNombre())
                .correo(prof.getUsuario().getEmail())
                .especialidad(prof.getEspecialidad())
                .build();
    }
}
