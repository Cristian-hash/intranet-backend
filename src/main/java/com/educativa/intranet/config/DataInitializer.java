package com.educativa.intranet.config;

import com.educativa.intranet.model.*;
import com.educativa.intranet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final ProfesorRepository profesorRepository;
    private final PadreRepository padreRepository;
    private final CursoRepository cursoRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final PeriodoRepository periodoRepository;
    private final AsignacionDocenteRepository asignacionDocenteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            System.out.println("\n==============================================");
            System.out.println("🚀 GENERANDO DATOS DE PRUEBA (MODELO v2)");
            System.out.println("==============================================");

            // 1. Crear Periodo de prueba
            Periodo periodo1 = Periodo.builder()
                    .nombre("1er Bimestre 2026")
                    .fechaInicio(LocalDate.of(2026, 3, 1))
                    .fechaFin(LocalDate.of(2026, 4, 30))
                    .activo(true)
                    .build();
            periodoRepository.save(periodo1);

            // 2. Crear Cursos con grado + sección + año
            Curso cursoMate1A = Curso.builder()
                    .nombre("Matemáticas").grado("1ro").seccion("A").anio(2026)
                    .profesores(new ArrayList<>())
                    .build();
            Curso cursoHistoria1A = Curso.builder()
                    .nombre("Historia").grado("1ro").seccion("A").anio(2026)
                    .profesores(new ArrayList<>())
                    .build();
            cursoRepository.save(cursoMate1A);
            cursoRepository.save(cursoHistoria1A);

            // 3. Crear Administrador (Director)
            Usuario admin = Usuario.builder()
                    .nombre("Director San Pedro")
                    .email("admin@colegio.edu")
                    .password(passwordEncoder.encode("admin123"))
                    .rol(Rol.ADMIN)
                    .build();
            usuarioRepository.save(admin);

            // 4. Crear Auxiliar
            Usuario auxiliarUser = Usuario.builder()
                    .nombre("Auxiliar García")
                    .email("auxiliar@colegio.edu")
                    .password(passwordEncoder.encode("auxiliar123"))
                    .rol(Rol.AUXILIAR)
                    .build();
            usuarioRepository.save(auxiliarUser);

            // 5. Crear Profesor + Perfil
            Usuario proUser = Usuario.builder()
                    .nombre("Profesor Einstein")
                    .email("profe@colegio.edu")
                    .password(passwordEncoder.encode("profe123"))
                    .rol(Rol.PROFESOR)
                    .build();
            usuarioRepository.save(proUser);

            Profesor profesor = Profesor.builder()
                    .usuario(proUser)
                    .especialidad("Matemáticas")
                    .cursos(new ArrayList<>())
                    .build();
            profesor.getCursos().add(cursoMate1A);
            profesorRepository.save(profesor);

            // 6. Crear AsignacionDocente (vincula profesor + curso + periodo)
            AsignacionDocente asignacion = AsignacionDocente.builder()
                    .profesor(profesor)
                    .curso(cursoMate1A)
                    .periodo(periodo1)
                    .activa(true)
                    .build();
            asignacionDocenteRepository.save(asignacion);

            // 7. Crear Padre + Perfil
            Usuario padreUser = Usuario.builder()
                    .nombre("Juan Pérez (Apoderado)")
                    .email("padre@colegio.edu")
                    .password(passwordEncoder.encode("padre123"))
                    .rol(Rol.PADRE)
                    .build();
            usuarioRepository.save(padreUser);

            Padre padre = Padre.builder()
                    .usuario(padreUser)
                    .build();
            padreRepository.save(padre);

            // 8. Crear Alumno con grado + sección
            Usuario alumnoUser = Usuario.builder()
                    .nombre("Pedrito Pérez")
                    .email("alumno@colegio.edu")
                    .password(passwordEncoder.encode("alumno123"))
                    .rol(Rol.ALUMNO)
                    .build();
            usuarioRepository.save(alumnoUser);

            Alumno alumno = Alumno.builder()
                    .usuario(alumnoUser)
                    .padre(padre)
                    .grado("1ro")
                    .seccion("A")
                    .build();
            alumnoRepository.save(alumno);

            System.out.println("✅ Entorno de datos v2 inicializado exitosamente.");
            System.out.println("🔒 Credenciales:");
            System.out.println("   ADMIN:    admin@colegio.edu    | admin123");
            System.out.println("   AUXILIAR: auxiliar@colegio.edu | auxiliar123");
            System.out.println("   PROFESOR: profe@colegio.edu   | profe123");
            System.out.println("   ALUMNO:   alumno@colegio.edu  | alumno123");
            System.out.println("   PADRE:    padre@colegio.edu   | padre123");
            System.out.println("==============================================\n");
        }

        // Seed de asistencia de prueba
        if (asistenciaRepository.count() == 0 && alumnoRepository.count() > 0) {
            Alumno alumno = alumnoRepository.findAll().get(0);
            Asistencia asist1 = Asistencia.builder()
                    .alumno(alumno)
                    .fecha(LocalDate.of(2026, 3, 5))
                    .estado(EstadoAsistencia.AUSENTE)
                    .build();
            Asistencia asist2 = Asistencia.builder()
                    .alumno(alumno)
                    .fecha(LocalDate.of(2026, 3, 6))
                    .estado(EstadoAsistencia.PRESENTE)
                    .build();
            asistenciaRepository.save(asist1);
            asistenciaRepository.save(asist2);
            System.out.println("✅ Asistencias de prueba generadas (Marzo 2026).");
        }
    }
}
