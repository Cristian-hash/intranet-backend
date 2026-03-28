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
import java.util.List;
import java.util.Random;

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
    private final MatriculaRepository matriculaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            System.out.println("\n==============================================");
            System.out.println("🚀 GENERANDO DATOS MASIVOS DE PRUEBA (v2.1)");
            System.out.println("==============================================");

            // 1. Periodo
            Periodo periodo1 = Periodo.builder()
                    .nombre("1er Bimestre 2026")
                    .fechaInicio(LocalDate.of(2026, 3, 1))
                    .fechaFin(LocalDate.of(2026, 4, 30))
                    .activo(true).build();
            periodoRepository.save(periodo1);

            // 2. Cursos
            Curso cursoMate1A = guardarCurso("Matemáticas", "1ro", "A");
            Curso cursoFisica1B = guardarCurso("Física", "1ro", "B");
            Curso cursoMate2A = guardarCurso("Geometría", "2do", "A");

            // 3. Admin & Auxiliar
            crearYGuardarUsuario("Director San Pedro", "admin@colegio.edu", "admin123", Rol.ADMIN);
            crearYGuardarUsuario("Auxiliar Gómez", "auxiliar@colegio.edu", "auxiliar123", Rol.AUXILIAR);

            // 4. Profesores (7 Docentes)
            Profesor prof1 = crearProfesor("Prof. Einstein", "profe1@colegio.edu", "Matemáticas");
            Profesor prof2 = crearProfesor("Prof. Tesla", "profe2@colegio.edu", "Física");
            Profesor prof3 = crearProfesor("Prof. Newton", "profe3@colegio.edu", "Geometría");
            Profesor prof4 = crearProfesor("Prof. Galileo", "profe4@colegio.edu", "Astronomía");
            Profesor prof5 = crearProfesor("Prof. Marie Curie", "profe5@colegio.edu", "Química");
            Profesor prof6 = crearProfesor("Prof. Alan Turing", "profe6@colegio.edu", "Computación");
            Profesor prof7 = crearProfesor("Prof. Ada Lovelace", "profe7@colegio.edu", "Programación");
            
            // Asignaciones Docentes (El Escenario de Prueba)
            // Asignaciones Correctas:
            asignacionDocenteRepository.save(AsignacionDocente.builder().profesor(prof1).curso(cursoMate1A).periodo(periodo1).activa(true).build());
            asignacionDocenteRepository.save(AsignacionDocente.builder().profesor(prof3).curso(cursoMate2A).periodo(periodo1).activa(true).build());
            
            // ASIGNACIÓN ERRÓNEA INTENCIONAL (Para que el Director pruebe a eliminarla):
            // La Profesora Curie (Química) fue asignada por error a Física 1B. El director debe borrarla y asignar a Tesla.
            asignacionDocenteRepository.save(AsignacionDocente.builder().profesor(prof5).curso(cursoFisica1B).periodo(periodo1).activa(true).build());

            // 5 y 6. Padres y Alumnos (Relación 1 a 1: Cada alumno tiene un papá distinto)
            List<Alumno> todosAlumnos = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                // Aula 1ro A
                Padre padreA = crearPadre("Padre " + i + " de 1ro A", "padre1a_" + i + "@colegio.edu");
                Alumno a1 = crearAlumno("Alumno A" + i, "alu1a_" + i + "@colegio.edu", padreA, "1ro", "A");
                matricular(a1, cursoMate1A);
                todosAlumnos.add(a1);

                // Aula 1ro B
                Padre padreB = crearPadre("Padre " + i + " de 1ro B", "padre1b_" + i + "@colegio.edu");
                Alumno a2 = crearAlumno("Alumno B" + i, "alu1b_" + i + "@colegio.edu", padreB, "1ro", "B");
                matricular(a2, cursoFisica1B);
                todosAlumnos.add(a2);

                // Aula 2do A
                Padre padreC = crearPadre("Padre " + i + " de 2do A", "padre2a_" + i + "@colegio.edu");
                Alumno a3 = crearAlumno("Alumno C" + i, "alu2a_" + i + "@colegio.edu", padreC, "2do", "A");
                matricular(a3, cursoMate2A);
                todosAlumnos.add(a3);
            }

            // 7. Simular 1 Semana de Asistencia (Lunes 2 de Marzo a Viernes 6 de Marzo 2026)
            System.out.println("⏳ Generando 1 semana de asistencias para 15 alumnos independientes...");
            Random random = new Random();
            EstadoAsistencia[] estados = {EstadoAsistencia.PRESENTE, EstadoAsistencia.PRESENTE, EstadoAsistencia.PRESENTE, EstadoAsistencia.TARDANZA, EstadoAsistencia.AUSENTE};
            
            for (int dia = 2; dia <= 6; dia++) {
                LocalDate fecha = LocalDate.of(2026, 3, dia);
                for (Alumno alu : todosAlumnos) {
                    EstadoAsistencia estadoRandom = estados[random.nextInt(estados.length)];
                    asistenciaRepository.save(Asistencia.builder()
                            .alumno(alu)
                            .fecha(fecha)
                            .estado(estadoRandom)
                            .build());
                }
            }

            System.out.println("✅ Entorno de Testeo Directivo Inicializado Exitosamente.");
            System.out.println("🔒 Cuentas para Probar Borrar/Asignar:");
            System.out.println("   admin@colegio.edu | admin123 (Verifica la profe Curie y elimínala, pon a Tesla)");
            System.out.println("🔒 Cuentas de Padres Únicos:");
            System.out.println("   padre1a_1@colegio.edu | padre123 (Papá del Alumno A1)");
            System.out.println("==============================================\n");
        }
    }

    private Usuario crearYGuardarUsuario(String nombre, String email, String pass, Rol rol) {
        Usuario u = Usuario.builder()
                .nombre(nombre).email(email).password(passwordEncoder.encode(pass)).rol(rol).build();
        return usuarioRepository.save(u);
    }

    private Curso guardarCurso(String nom, String grado, String sec) {
        return cursoRepository.save(Curso.builder().nombre(nom).grado(grado).seccion(sec).anio(2026).profesores(new ArrayList<>()).build());
    }

    private Profesor crearProfesor(String nom, String email, String espe) {
        Usuario u = crearYGuardarUsuario(nom, email, "profe123", Rol.PROFESOR);
        return profesorRepository.save(Profesor.builder().usuario(u).especialidad(espe).cursos(new ArrayList<>()).build());
    }

    private Padre crearPadre(String nom, String email) {
        Usuario u = crearYGuardarUsuario(nom, email, "padre123", Rol.PADRE);
        return padreRepository.save(Padre.builder().usuario(u).build());
    }

    private Alumno crearAlumno(String nom, String email, Padre p, String grado, String sec) {
        Usuario u = crearYGuardarUsuario(nom, email, "alumno123", Rol.ALUMNO);
        return alumnoRepository.save(Alumno.builder().usuario(u).padre(p).grado(grado).seccion(sec).build());
    }

    private void matricular(Alumno a, Curso c) {
        matriculaRepository.save(Matricula.builder().alumno(a).curso(c).activo(true).build());
    }
}
