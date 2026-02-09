package com.devsenior.manuel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsenior.manuel.exception.CursoLlenoException;
import com.devsenior.manuel.exception.EstudianteDuplicadoException;
import com.devsenior.manuel.exception.EstudianteNoEncontradoException;
import com.devsenior.manuel.model.Curso;
import com.devsenior.manuel.model.Estudiante;
import com.devsenior.manuel.service.InscripcionService;

public class InscripcionServiceTest {

    private InscripcionService inscripcionService;

    @BeforeEach
    public void setUp() {
        inscripcionService = new InscripcionService();
    }

    @Test
    public void agregarInscripcionCorrectamente() {
        // Implementar prueba para agregar una inscripción correctamente
        Estudiante estudiante = new Estudiante("Ana Martínez", "99887766", "anam@gmail.com");
        Curso curso = new Curso("Literatura", "LIT101", 2);
        inscripcionService.agregarEstudiante(curso, estudiante, LocalDate.now());
        // Aquí se podrían agregar aserciones para verificar que la inscripción fue agregada
        assertEquals(1, curso.getInscripciones().size());
        assertEquals(estudiante, curso.getInscripciones().get(0).getEstudiante());
        assertEquals(curso, curso.getInscripciones().get(0).getCurso());
    }

    // Implementar pruebas para cuando el curso está lleno
    @Test
    public void agregarInscripcionCursoLleno() {
        Estudiante estudiante1 = new Estudiante("Ana Martínez", "99887766", "anam@gmail.com");
        Estudiante estudiante2 = new Estudiante("Carlos Rodríguez", "11223344", "carlosr@gmail.com");
        Estudiante estudiante3 = new Estudiante("Laura Sánchez", "55667788", "lauras@gmail.com");
        Curso curso = new Curso("Física", "FIS101", 2);
        inscripcionService.agregarEstudiante(curso, estudiante1, LocalDate.now());
        inscripcionService.agregarEstudiante(curso, estudiante2, LocalDate.now());
        // Intentar agregar un tercer estudiante al curso lleno
        assertThrows(CursoLlenoException.class, () -> inscripcionService.agregarEstudiante(curso, estudiante3, LocalDate.now()));
        assertEquals(2, curso.getInscripciones().size());
    }

    // Implementar pruebas para eliminar una inscripción
    @Test
    public void eliminarInscripcion() throws EstudianteNoEncontradoException {
        Estudiante estudiante = new Estudiante("Ana Martínez", "99887766", "anam@gmail.com");
        Curso curso = new Curso("Literatura", "LIT101", 2);
        inscripcionService.agregarEstudiante(curso, estudiante, LocalDate.now());
        assertEquals(1, curso.getInscripciones().size());
        inscripcionService.eliminarEstudiante(curso, estudiante);
        assertEquals(0, curso.getInscripciones().size());
    }

    // Implementar pruebas para eliminar una inscripción que no existe
    @Test
    public void eliminarInscripcionNoExistente() {
        Estudiante estudiante = new Estudiante("Ana Martínez", "99887766", "anam@gmail.com");
        Curso curso = new Curso("Literatura", "LIT101", 2);
        // Intentar eliminar un estudiante que no está inscrito en el curso
        assertThrows(EstudianteNoEncontradoException.class, () -> {
            inscripcionService.eliminarEstudiante(curso, estudiante);
        });
    }

    // Implementar pruebas para listar inscripciones por estudiante
    @Test
    public void listarInscripcionesPorEstudiante() throws EstudianteNoEncontradoException {
        Estudiante estudiante = new Estudiante("Ana Martínez", "99887766", "anam@gmail.com");
        Curso curso1 = new Curso("Literatura", "LIT101", 2);
        Curso curso2 = new Curso("Física", "FIS101", 2);
        inscripcionService.agregarEstudiante(curso1, estudiante, LocalDate.now());
        inscripcionService.agregarEstudiante(curso2, estudiante, LocalDate.now());

        assertEquals(2, inscripcionService.ListarInscripcionesPorEstudiante(estudiante).size());
        assertEquals(curso1, inscripcionService.ListarInscripcionesPorEstudiante(estudiante).get(0).getCurso());
        assertEquals(curso2, inscripcionService.ListarInscripcionesPorEstudiante(estudiante).get(1).getCurso());
    }

    @Test
    public void agregarEstudianteNull() {
        Curso curso = new Curso("Física", "FIS101", 2);

        assertThrows(IllegalArgumentException.class, () -> {
            inscripcionService.agregarEstudiante(curso, null, LocalDate.now());
        });
    }

    @Test
    public void agregarCursoNull() {
        Estudiante estudiante = new Estudiante("Ana", "123", "ana@test.com");

        assertThrows(IllegalArgumentException.class, () -> {
            inscripcionService.agregarEstudiante(null, estudiante, LocalDate.now());
        });
    }

    @Test
    public void agregarFechaNull() {
        Estudiante estudiante = new Estudiante("Ana", "123", "ana@test.com");
        Curso curso = new Curso("Física", "FIS101", 2);

        assertThrows(IllegalArgumentException.class, () -> {
            inscripcionService.agregarEstudiante(curso, estudiante, null);
        });
    }

    @Test
    public void agregarEstudianteDuplicado() {
        Estudiante estudiante = new Estudiante("Ana", "123", "ana@test.com");
        Curso curso = new Curso("Física", "FIS101", 2);

        inscripcionService.agregarEstudiante(curso, estudiante, LocalDate.now());

        // Intentar inscribir el mismo estudiante al mismo curso
        assertThrows(EstudianteDuplicadoException.class, () -> {
            inscripcionService.agregarEstudiante(curso, estudiante, LocalDate.now());
        });
    }

    @Test
    public void eliminarDeListaVacia() {
        Estudiante estudiante = new Estudiante("Ana", "123", "ana@test.com");
        Curso curso = new Curso("Física", "FIS101", 2);

        assertThrows(EstudianteNoEncontradoException.class, () -> {
            inscripcionService.eliminarEstudiante(curso, estudiante);
        });
    }
}
