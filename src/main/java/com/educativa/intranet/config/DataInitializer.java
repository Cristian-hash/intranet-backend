package com.educativa.intranet.config;

import com.educativa.intranet.model.*;
import com.educativa.intranet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final ProfesorRepository profesorRepository;
    private final PadreRepository padreRepository;
    private final CursoRepository cursoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Ejecutar solo si la base de datos está completamente vacía
        if (usuarioRepository.count() == 0) {
            System.out.println("\n==============================================");
            System.out.println("🚀 GENERANDO DATOS DE PRUEBA (DATA.SQL ALTERNATIVE)");
            System.out.println("==============================================");

            // 1. Crear Cursos Iniciales
            Curso cursoMate = Curso.builder().nombre("Matemáticas Avanzadas").profesores(new ArrayList<>()).build();
            Curso cursoHistoria = Curso.builder().nombre("Historia Universal").profesores(new ArrayList<>()).build();
            cursoRepository.save(cursoMate);
            cursoRepository.save(cursoHistoria);

            // 2. Crear Administrador Maestro
            Usuario admin = Usuario.builder()
                    .nombre("Administrador Principal")
                    .email("admin@colegio.edu")
                    .password(passwordEncoder.encode("admin123"))
                    .rol(Rol.ADMIN)
                    .build();
            usuarioRepository.save(admin);

            // 3. Crear Perfil de Profesor
            Usuario proUser = Usuario.builder()
                    .nombre("Profesor Einstein")
                    .email("profe@colegio.edu")
                    .password(passwordEncoder.encode("profe123"))
                    .rol(Rol.PROFESOR)
                    .build();
            usuarioRepository.save(proUser);
            
            Profesor profesor = Profesor.builder()
                    .usuario(proUser)
                    .cursos(new ArrayList<>())
                    .build();
            // Regla Cumplida: Este profesor SOLAMENTE dicta Matemáticas, ¡no puede calificar Historia!
            profesor.getCursos().add(cursoMate); 
            profesorRepository.save(profesor);

            // 4. Crear Perfil de Padre
            Usuario padreUser = Usuario.builder()
                    .nombre("Juan Perez (Apoderado)")
                    .email("padre@colegio.edu")
                    .password(passwordEncoder.encode("padre123"))
                    .rol(Rol.PADRE)
                    .build();
            usuarioRepository.save(padreUser);

            Padre padre = Padre.builder()
                    .usuario(padreUser)
                    .build();
            padreRepository.save(padre);

            // 5. Crear Perfil de Alumno y Vincular a Padre
            Usuario alumnoUser = Usuario.builder()
                    .nombre("Pedrito Perez (Alumno)")
                    .email("alumno@colegio.edu")
                    .password(passwordEncoder.encode("alumno123"))
                    .rol(Rol.ALUMNO)
                    .build();
            usuarioRepository.save(alumnoUser);

            Alumno alumno = Alumno.builder()
                    .usuario(alumnoUser)
                    .padre(padre)
                    .build();
            alumnoRepository.save(alumno);

            System.out.println("✅ Entorno de datos inicializado exitosamente.");
            System.out.println("🔒 Credenciales para Swagger:");
            System.out.println("   ADMIN:    admin@colegio.edu  | pass: admin123");
            System.out.println("   PROFESOR: profe@colegio.edu  | pass: profe123");
            System.out.println("   ALUMNO:   alumno@colegio.edu | pass: alumno123");
            System.out.println("   PADRE:    padre@colegio.edu  | pass: padre123");
            System.out.println("==============================================\n");
        }
    }
}
