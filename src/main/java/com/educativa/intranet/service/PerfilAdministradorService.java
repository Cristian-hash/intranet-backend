package com.educativa.intranet.service;

import com.educativa.intranet.dto.*;
import com.educativa.intranet.model.*;
import com.educativa.intranet.repository.AlumnoRepository;
import com.educativa.intranet.repository.PadreRepository;
import com.educativa.intranet.repository.ProfesorRepository;
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
    private final ProfesorRepository profesorRepository; // Agregado para el CRUD de Profesores

    // ==========================================
    // 1. GESTIÓN FÍSICA DE ALUMNOS
    // ==========================================
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
                .seccion(dto.getSeccion())
                .fechaNacimiento(dto.getFechaNacimiento())
                .build();

        alumnoRepository.save(alumnoFisico);
        return mapearADTO(alumnoFisico);
    }

    @Transactional(readOnly = true)
    public List<AlumnoPerfilResponseDTO> listarTodosLosAlumnosCompletos() {
        return alumnoRepository.findAll().stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    // ==========================================
    // 2. GESTIÓN FÍSICA DE PROFESORES
    // ==========================================
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

    @Transactional(readOnly = true)
    public List<ProfesorPerfilResponseDTO> listarTodosLosProfesoresCompletos() {
        return profesorRepository.findAll().stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    // ==========================================
    // 3. GESTIÓN FÍSICA DE PADRES
    // ==========================================
    @Transactional
    public PadrePerfilResponseDTO construirPerfilPadre(PadrePerfilCreateDTO dto) {
        Usuario usuario = buscarYValidarUsuario(dto.getUsuarioId(), Rol.PADRE);

        Padre padreFisico = Padre.builder()
                .usuario(usuario)
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .build();

        padreRepository.save(padreFisico);
        return mapearADTO(padreFisico);
    }

    @Transactional(readOnly = true)
    public List<PadrePerfilResponseDTO> listarTodosLosPadresCompletos() {
        return padreRepository.findAll().stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    // ==========================================
    // FUNCIONES AUXILIARES INTERNAS (EL CIRUJANO)
    // ==========================================
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

    private ProfesorPerfilResponseDTO mapearADTO(Profesor prof) {
        return ProfesorPerfilResponseDTO.builder()
                .perfilId(prof.getId())
                .usuarioId(prof.getUsuario().getId())
                .nombre(prof.getUsuario().getNombre())
                .correo(prof.getUsuario().getEmail())
                .especialidad(prof.getEspecialidad())
                .build();
    }

    private PadrePerfilResponseDTO mapearADTO(Padre pa) {
        return PadrePerfilResponseDTO.builder()
                .perfilId(pa.getId())
                .usuarioId(pa.getUsuario().getId())
                .nombre(pa.getUsuario().getNombre())
                .correo(pa.getUsuario().getEmail())
                .telefono(pa.getTelefono())
                .direccion(pa.getDireccion())
                .build();
    }
}
