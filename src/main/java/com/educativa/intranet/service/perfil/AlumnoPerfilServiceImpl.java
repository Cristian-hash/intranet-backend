package com.educativa.intranet.service.perfil;

import com.educativa.intranet.dto.AlumnoPerfilCreateDTO;
import com.educativa.intranet.dto.AlumnoPerfilResponseDTO;
import com.educativa.intranet.model.Alumno;
import com.educativa.intranet.model.Padre;
import com.educativa.intranet.model.Rol;
import com.educativa.intranet.model.Usuario;
import com.educativa.intranet.repository.AlumnoRepository;
import com.educativa.intranet.repository.PadreRepository;
import com.educativa.intranet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlumnoPerfilServiceImpl implements IAlumnoPerfilService {

    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final PadreRepository padreRepository;

    @Override
    @Transactional
    public AlumnoPerfilResponseDTO construirPerfilAlumno(AlumnoPerfilCreateDTO dto) {
        Usuario usuario = buscarYValidarUsuario(dto.getUsuarioId(), Rol.ALUMNO);

        Padre padreAsignado = null;
        if (dto.getPadreId() != null) {
            padreAsignado = padreRepository.findById(dto.getPadreId())
                    .orElseThrow(() -> new RuntimeException("El Padre indicado no existe."));
        }

        Alumno alumnoFisico = Alumno.builder()
                .usuario(usuario)
                .padre(padreAsignado)
                .grado(dto.getGrado())
                .fechaNacimiento(dto.getFechaNacimiento())
                .build();

        alumnoRepository.save(alumnoFisico);
        return mapearADTO(alumnoFisico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoPerfilResponseDTO> listarTodosLosAlumnosCompletos() {
        return alumnoRepository.findAll().stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    private Usuario buscarYValidarUsuario(Long usuarioId, Rol rolEsperado) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Ese usuario matriz no existe."));

        if (!usuario.getRol().equals(rolEsperado)) {
            throw new RuntimeException("Regla de RRHH: Intentas crear un perfil de " + rolEsperado + " sobre un usuario que es " + usuario.getRol());
        }
        return usuario;
    }

    private AlumnoPerfilResponseDTO mapearADTO(Alumno al) {
        return AlumnoPerfilResponseDTO.builder()
                .perfilId(al.getId())
                .usuarioId(al.getUsuario().getId())
                .nombre(al.getUsuario().getNombre())
                .correo(al.getUsuario().getEmail())
                .grado(al.getGrado())
                .fechaNacimiento(al.getFechaNacimiento())
                .nombrePadre(al.getPadre() != null ? al.getPadre().getUsuario().getNombre() : "Huérfano/No Asignado")
                .build();
    }
}
