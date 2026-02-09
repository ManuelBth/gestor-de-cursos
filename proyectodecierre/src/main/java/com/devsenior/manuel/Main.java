package com.devsenior.manuel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.*;

import com.devsenior.manuel.exception.CursoDuplicadoException;
import com.devsenior.manuel.exception.CursoLlenoException;
import com.devsenior.manuel.exception.EstudianteDuplicadoException;
import com.devsenior.manuel.exception.EstudianteNoEncontradoException;
import com.devsenior.manuel.model.Curso;
import com.devsenior.manuel.model.Estudiante;
import com.devsenior.manuel.model.Inscripcion;
import com.devsenior.manuel.service.CursoService;
import com.devsenior.manuel.service.InscripcionService;

public class Main {
    
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static Scanner scanner = new Scanner(System.in);
    
    // Servicios
    private static CursoService cursoService = new CursoService();
    private static InscripcionService inscripcionService = new InscripcionService();
    
    // Lista de estudiantes en memoria
    private static List<Estudiante> estudiantes = new ArrayList<>();
    
    public static void main(String[] args) {
        logger.info("Application started");
        System.out.println("=== SISTEMA DE INSCRIPCIONES ===");
        System.out.println("Bienvenido al sistema de gestión de cursos y estudiantes\n");
        
        boolean salir = false;
        
        while (!salir) {
            mostrarMenu();
            int opcion = leerOpcion();
            
            try {
                switch (opcion) {
                    case 1:
                        crearEstudiante();
                        break;
                    case 2:
                        crearCurso();
                        break;
                    case 3:
                        agregarEstudianteACurso();
                        break;
                    case 4:
                        listarCursosPorEstudiante();
                        break;
                    case 5:
                        listarEstudiantesDeCurso();
                        break;
                    case 6:
                        eliminarEstudianteDeCurso();
                        break;
                    case 7:
                        salir = true;
                        System.out.println("\n¡Gracias por usar el Sistema de Inscripciones!");
                        logger.info("Application terminated by user");
                        break;
                    default:
                        System.out.println("\n⚠️ Opción no válida. Por favor, seleccione una opción del 1 al 7.");
                }
            } catch (Exception e) {
                logger.error("Error in menu operation: " + e.getMessage(), e);
                System.out.println("\n❌ Error: " + e.getMessage());
            }
            
            if (!salir) {
                System.out.println("\nPresione ENTER para continuar...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void mostrarMenu() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║         MENÚ PRINCIPAL             ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ 1. Crear Estudiante                ║");
        System.out.println("║ 2. Crear Curso                     ║");
        System.out.println("║ 3. Agregar Estudiante a Curso      ║");
        System.out.println("║ 4. Listar Cursos por Estudiante    ║");
        System.out.println("║ 5. Listar Estudiantes de un Curso  ║");
        System.out.println("║ 6. Eliminar Estudiante de Curso    ║");
        System.out.println("║ 7. Salir                           ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.print("\nSeleccione una opción: ");
    }
    
    private static int leerOpcion() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void crearEstudiante() {
        System.out.println("\n--- CREAR ESTUDIANTE ---");
        
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine().trim();
        
        if (nombre.isEmpty()) {
            System.out.println("❌ El nombre no puede estar vacío.");
            return;
        }
        
        System.out.print("ID (cédula/código único): ");
        String id = scanner.nextLine().trim();
        
        if (id.isEmpty()) {
            System.out.println("❌ El ID no puede estar vacío.");
            return;
        }
        
        // Verificar si ya existe un estudiante con ese ID
        for (Estudiante e : estudiantes) {
            if (e.getId().equals(id)) {
                System.out.println("❌ Ya existe un estudiante con el ID: " + id);
                return;
            }
        }
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        if (email.isEmpty()) {
            System.out.println("❌ El email no puede estar vacío.");
            return;
        }
        
        Estudiante estudiante = new Estudiante(nombre, id, email);
        estudiantes.add(estudiante);
        
        logger.info("Student created: " + estudiante);
        System.out.println("\n✅ Estudiante creado exitosamente:");
        System.out.println("   Nombre: " + nombre);
        System.out.println("   ID: " + id);
        System.out.println("   Email: " + email);
    }
    
    private static void crearCurso() {
        System.out.println("\n--- CREAR CURSO ---");
        
        System.out.print("Nombre del curso: ");
        String nombre = scanner.nextLine().trim();
        
        if (nombre.isEmpty()) {
            System.out.println("❌ El nombre no puede estar vacío.");
            return;
        }
        
        System.out.print("Código único: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        
        if (codigo.isEmpty()) {
            System.out.println("❌ El código no puede estar vacío.");
            return;
        }
        
        System.out.print("Capacidad máxima de estudiantes: ");
        int capacidad;
        try {
            capacidad = Integer.parseInt(scanner.nextLine().trim());
            if (capacidad <= 0) {
                System.out.println("❌ La capacidad debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ La capacidad debe ser un número válido.");
            return;
        }
        
        try {
            Curso curso = new Curso(nombre, codigo, capacidad);
            cursoService.agregarCurso(curso);
            
            logger.info("Course created: " + curso);
            System.out.println("\n✅ Curso creado exitosamente:");
            System.out.println("   Nombre: " + nombre);
            System.out.println("   Código: " + codigo);
            System.out.println("   Capacidad: " + capacidad + " estudiantes");
        } catch (CursoDuplicadoException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    private static void agregarEstudianteACurso() {
        System.out.println("\n--- AGREGAR ESTUDIANTE A CURSO ---");
        
        // Mostrar estudiantes disponibles
        if (estudiantes.isEmpty()) {
            System.out.println("❌ No hay estudiantes registrados. Cree un estudiante primero (Opción 1).");
            return;
        }
        
        // Mostrar cursos disponibles
        List<Curso> cursos = cursoService.getCursos();
        if (cursos.isEmpty()) {
            System.out.println("❌ No hay cursos registrados. Cree un curso primero (Opción 2).");
            return;
        }
        
        System.out.println("\nEstudiantes disponibles:");
        mostrarEstudiantes();
        
        System.out.print("\nIngrese el ID del estudiante: ");
        String idEstudiante = scanner.nextLine().trim();
        
        Estudiante estudiante = buscarEstudiantePorId(idEstudiante);
        if (estudiante == null) {
            System.out.println("❌ No se encontró un estudiante con el ID: " + idEstudiante);
            return;
        }
        
        System.out.println("\nCursos disponibles:");
        mostrarCursos();
        
        System.out.print("\nIngrese el código del curso: ");
        String codigoCurso = scanner.nextLine().trim().toUpperCase();
        
        Curso curso;
        try {
            curso = cursoService.getCursoPorId(codigoCurso);
        } catch (Exception e) {
            System.out.println("❌ No se encontró un curso con el código: " + codigoCurso);
            return;
        }
        
        // Solicitar fecha de inscripción
        System.out.print("Fecha de inscripción (YYYY-MM-DD) [Enter para hoy]: ");
        String fechaStr = scanner.nextLine().trim();
        
        LocalDate fechaInscripcion;
        if (fechaStr.isEmpty()) {
            fechaInscripcion = LocalDate.now();
        } else {
            try {
                fechaInscripcion = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato de fecha inválido. Use YYYY-MM-DD.");
                return;
            }
        }
        
        try {
            inscripcionService.agregarEstudiante(curso, estudiante, fechaInscripcion);
            
            System.out.println("\n✅ Inscripción exitosa:");
            System.out.println("   Estudiante: " + estudiante.getNombre());
            System.out.println("   Curso: " + curso.getNombre());
            System.out.println("   Fecha: " + fechaInscripcion);
            System.out.println("   Cupos disponibles: " + (curso.getCapacidad() - curso.getInscripciones().size()));
        } catch (CursoLlenoException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (EstudianteDuplicadoException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    private static void listarCursosPorEstudiante() {
        System.out.println("\n--- LISTAR CURSOS POR ESTUDIANTE ---");
        
        if (estudiantes.isEmpty()) {
            System.out.println("❌ No hay estudiantes registrados.");
            return;
        }
        
        System.out.println("\nEstudiantes registrados:");
        mostrarEstudiantes();
        
        System.out.print("\nIngrese el ID del estudiante: ");
        String idEstudiante = scanner.nextLine().trim();
        
        Estudiante estudiante = buscarEstudiantePorId(idEstudiante);
        if (estudiante == null) {
            System.out.println("❌ No se encontró un estudiante con el ID: " + idEstudiante);
            return;
        }
        
        try {
            List<Inscripcion> inscripciones = inscripcionService.ListarInscripcionesPorEstudiante(estudiante);
            
            System.out.println("\n📚 Cursos inscritos por " + estudiante.getNombre() + ":");
            System.out.println("═══════════════════════════════════════════════════");
            
            for (int i = 0; i < inscripciones.size(); i++) {
                Inscripcion insc = inscripciones.get(i);
                System.out.println("  " + (i + 1) + ". " + insc.getCurso().getNombre());
                System.out.println("     Código: " + insc.getCurso().getCodigo());
                System.out.println("     Fecha de inscripción: " + insc.getFechaInscripcion());
                System.out.println();
            }
            
            System.out.println("Total de cursos: " + inscripciones.size());
            
        } catch (EstudianteNoEncontradoException e) {
            System.out.println("\n📋 El estudiante " + estudiante.getNombre() + " no está inscrito en ningún curso.");
        }
    }
    
    private static void listarEstudiantesDeCurso() {
        System.out.println("\n--- LISTAR ESTUDIANTES DE UN CURSO ---");
        
        List<Curso> cursos = cursoService.getCursos();
        if (cursos.isEmpty()) {
            System.out.println("❌ No hay cursos registrados.");
            return;
        }
        
        System.out.println("\nCursos disponibles:");
        mostrarCursos();
        
        System.out.print("\nIngrese el código del curso: ");
        String codigoCurso = scanner.nextLine().trim().toUpperCase();
        
        Curso curso;
        try {
            curso = cursoService.getCursoPorId(codigoCurso);
        } catch (Exception e) {
            System.out.println("❌ No se encontró un curso con el código: " + codigoCurso);
            return;
        }
        
        List<Inscripcion> inscripciones = curso.getInscripciones();
        
        if (inscripciones.isEmpty()) {
            System.out.println("\n📋 El curso " + curso.getNombre() + " no tiene estudiantes inscritos.");
            System.out.println("   Capacidad: " + curso.getCapacidad() + " estudiantes");
            System.out.println("   Inscritos: 0");
            return;
        }
        
        System.out.println("\n👥 Estudiantes inscritos en " + curso.getNombre() + ":");
        System.out.println("═══════════════════════════════════════════════════");
        
        for (int i = 0; i < inscripciones.size(); i++) {
            Inscripcion insc = inscripciones.get(i);
            Estudiante est = insc.getEstudiante();
            System.out.println("  " + (i + 1) + ". " + est.getNombre());
            System.out.println("     ID: " + est.getId());
            System.out.println("     Email: " + est.getEmail());
            System.out.println("     Fecha de inscripción: " + insc.getFechaInscripcion());
            System.out.println();
        }
        
        System.out.println("Inscritos: " + inscripciones.size() + " de " + curso.getCapacidad() + " cupos");
        System.out.println("Disponibles: " + (curso.getCapacidad() - inscripciones.size()) + " cupos");
    }
    
    private static void eliminarEstudianteDeCurso() {
        System.out.println("\n--- ELIMINAR ESTUDIANTE DE CURSO ---");
        
        if (estudiantes.isEmpty()) {
            System.out.println("❌ No hay estudiantes registrados.");
            return;
        }
        
        List<Curso> cursos = cursoService.getCursos();
        if (cursos.isEmpty()) {
            System.out.println("❌ No hay cursos registrados.");
            return;
        }
        
        // Verificar si hay inscripciones
        boolean hayInscripciones = false;
        for (Curso c : cursos) {
            if (!c.getInscripciones().isEmpty()) {
                hayInscripciones = true;
                break;
            }
        }
        
        if (!hayInscripciones) {
            System.out.println("❌ No hay inscripciones registradas en el sistema.");
            return;
        }
        
        System.out.println("\nEstudiantes registrados:");
        mostrarEstudiantes();
        
        System.out.print("\nIngrese el ID del estudiante: ");
        String idEstudiante = scanner.nextLine().trim();
        
        Estudiante estudiante = buscarEstudiantePorId(idEstudiante);
        if (estudiante == null) {
            System.out.println("❌ No se encontró un estudiante con el ID: " + idEstudiante);
            return;
        }
        
        // Mostrar en qué cursos está inscrito
        List<Curso> cursosDelEstudiante = new ArrayList<>();
        for (Curso c : cursos) {
            for (Inscripcion insc : c.getInscripciones()) {
                if (insc.getEstudiante().getId().equals(idEstudiante)) {
                    cursosDelEstudiante.add(c);
                    break;
                }
            }
        }
        
        if (cursosDelEstudiante.isEmpty()) {
            System.out.println("❌ El estudiante " + estudiante.getNombre() + " no está inscrito en ningún curso.");
            return;
        }
        
        System.out.println("\nCursos donde está inscrito " + estudiante.getNombre() + ":");
        for (int i = 0; i < cursosDelEstudiante.size(); i++) {
            Curso c = cursosDelEstudiante.get(i);
            System.out.println("  " + (i + 1) + ". " + c.getNombre() + " (" + c.getCodigo() + ")");
        }
        
        System.out.print("\nIngrese el código del curso del cual desea eliminar al estudiante: ");
        String codigoCurso = scanner.nextLine().trim().toUpperCase();
        
        Curso curso;
        try {
            curso = cursoService.getCursoPorId(codigoCurso);
        } catch (Exception e) {
            System.out.println("❌ No se encontró un curso con el código: " + codigoCurso);
            return;
        }
        
        // Verificar si el estudiante está en ese curso
        boolean estaInscrito = false;
        for (Inscripcion insc : curso.getInscripciones()) {
            if (insc.getEstudiante().getId().equals(idEstudiante)) {
                estaInscrito = true;
                break;
            }
        }
        
        if (!estaInscrito) {
            System.out.println("❌ El estudiante " + estudiante.getNombre() + " no está inscrito en el curso " + curso.getNombre());
            return;
        }
        
        System.out.print("\n⚠️ ¿Está seguro de eliminar a " + estudiante.getNombre() + " del curso " + curso.getNombre() + "? (S/N): ");
        String confirmacion = scanner.nextLine().trim().toUpperCase();
        
        if (!confirmacion.equals("S")) {
            System.out.println("\n✓ Operación cancelada.");
            return;
        }
        
        try {
            inscripcionService.eliminarEstudiante(curso, estudiante);
            System.out.println("\n✅ Estudiante eliminado exitosamente:");
            System.out.println("   Estudiante: " + estudiante.getNombre());
            System.out.println("   Curso: " + curso.getNombre());
            System.out.println("   Cupos disponibles ahora: " + (curso.getCapacidad() - curso.getInscripciones().size()));
        } catch (EstudianteNoEncontradoException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    // Helper methods
    private static void mostrarEstudiantes() {
        System.out.println("─────────────────────────────────────────────────");
        for (Estudiante e : estudiantes) {
            System.out.println("  ID: " + e.getId() + " - " + e.getNombre());
        }
        System.out.println("─────────────────────────────────────────────────");
        System.out.println("Total: " + estudiantes.size() + " estudiantes");
    }
    
    private static void mostrarCursos() {
        List<Curso> cursos = cursoService.getCursos();
        System.out.println("─────────────────────────────────────────────────");
        for (Curso c : cursos) {
            int inscritos = c.getInscripciones().size();
            int disponibles = c.getCapacidad() - inscritos;
            System.out.println("  Código: " + c.getCodigo() + " - " + c.getNombre());
            System.out.println("         Cupos: " + inscritos + "/" + c.getCapacidad() + " (Disponibles: " + disponibles + ")");
        }
        System.out.println("─────────────────────────────────────────────────");
        System.out.println("Total: " + cursos.size() + " cursos");
    }
    
    private static Estudiante buscarEstudiantePorId(String id) {
        for (Estudiante e : estudiantes) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }
}
