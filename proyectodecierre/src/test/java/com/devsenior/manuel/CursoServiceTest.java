package com.devsenior.manuel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsenior.manuel.exception.CursoDuplicadoException;
import com.devsenior.manuel.exception.CursoNoEncontradoException;
import com.devsenior.manuel.model.Curso;
import com.devsenior.manuel.service.CursoService;

public class CursoServiceTest {

    private CursoService cursoService;

    @BeforeEach
    public void setUp() {
        cursoService = new CursoService();
    }

    @Test
    public void agregarCursoCorrectamente() {
        // Implementar prueba para agregar un curso correctamente
        Curso curso = new Curso("Física", "FIS101", 2);
        Curso curso2 = new Curso("Química", "QUI101", 3);
        cursoService.agregarCurso(curso);
        cursoService.agregarCurso(curso2);
        // Aquí se podrían agregar aserciones para verificar que el curso fue agregado
        assertEquals(2, cursoService.getCursos().size());

        // Aqui se comprueba que se agregaron dos cursos por los dos nombres
        assertEquals("Física", cursoService.getCursos().get(0).getNombre());
        assertEquals("Química", cursoService.getCursos().get(1).getNombre());    
    }

    @Test
    public void listarCursoVacio() {
        // Implementar prueba para listar cursos cuando no hay cursos agregados
        assertEquals(0, cursoService.getCursos().size());
    }

    @Test
    public void buscarCursoPorIdExistente() {
        // Implementar prueba para buscar varios cursos por ID existente
        Curso curso1 = new Curso("Biología", "BIO101", 2);
        Curso curso2 = new Curso("Historia", "HIS101", 3);
        cursoService.agregarCurso(curso1);
        cursoService.agregarCurso(curso2);
        assertEquals("Biología", cursoService.getCursoPorId("BIO101").getNombre());
        assertEquals("Historia", cursoService.getCursoPorId("HIS101").getNombre());
    }

    @Test
    public void buscarCursoPorIdInexistente() {
        // Implementar prueba para buscar un curso por ID inexistente
        assertThrows(CursoNoEncontradoException.class, () -> {
            cursoService.getCursoPorId("MAT999");
        });
    }

    @Test
    public void agregarCursoNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            cursoService.agregarCurso(null);
        });
    }

    @Test
    public void agregarCursoDuplicado() {
        Curso curso = new Curso("Física", "FIS101", 2);
        cursoService.agregarCurso(curso);
        
        Curso cursoDuplicado = new Curso("Física II", "FIS101", 3); // Mismo código
        
        assertThrows(CursoDuplicadoException.class, () -> {
            cursoService.agregarCurso(cursoDuplicado);
        });
    }

}
