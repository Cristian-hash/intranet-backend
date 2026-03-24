package com.educativa.intranet.service;

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
public class PerfilAdministradorService {

    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final PadreRepository padreRepository;

    @Transactional
    public AlumnoPerfilResponseDTO construirPerfilAlumno(AlumnoPerfilCreateDTO dto) {
        // 1. Verificar si el usuario matriz existe.
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Ese usuario base no existe."));

        // 2. Verificar regla de negocio: ¿Ese usuario base en verdad dice "ALUMNO" en su gafete?
        if (!usuario.getRol().equals(Rol.ALUMNO)) {
            throw new RuntimeException("Regla de RRHH: No puedes empujar un perfil de Alumno sobre la cuenta de un Profesor o Padre.");
        }

        // 3. Opcional: ¿Lo vas a vincular con su padre de una vez?
        Padre padreAsignado = null;
        if (dto.getPadreId() != null) {
            padreAsignado = padreRepository.findById(dto.getPadreId())
                    .orElseThrow(() -> new RuntimeException("El Padre indicado no existe en la base de datos."));
        }

        // 4. Vaciando el concreto (Entity):
        Alumno alumnoFisico = Alumno.builder()
                .usuario(usuario)
                .padre(padreAsignado)
                .grado(dto.getGrado())
                .fechaNacimiento(dto.getFechaNacimiento())
                .build();

        alumnoRepository.save(alumnoFisico);

        return mapearADTO(alumnoFisico);
    }

    @Transactional(readOnly = true)
    public List<AlumnoPerfilResponseDTO> listarTodosLosAlumnosCompletos() {
        return alumnoRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    private AlumnoPerfilResponseDTO mapearADTO(Alumno al) {
        return AlumnoPerfilResponseDTO.builder()
                .perfilId(al.getId())
                .usuarioId(al.getUsuario().getId())
                .nombre(al.getUsuario().getNombre()) // Obtenemos el nombre cruzando hacia IAM
                .correo(al.getUsuario().getEmail())
                .grado(al.getGrado())
                .fechaNacimiento(al.getFechaNacimiento())
                .nombrePadre(al.getPadre() != null ? al.getPadre().getUsuario().getNombre() : "Huérfano/No Asignado")
                .build();
    }
}
