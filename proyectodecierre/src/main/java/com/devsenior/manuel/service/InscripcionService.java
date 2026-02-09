package com.devsenior.manuel.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.*;

import com.devsenior.manuel.exception.CursoLlenoException;
import com.devsenior.manuel.exception.EstudianteDuplicadoException;
import com.devsenior.manuel.exception.EstudianteNoEncontradoException;
import com.devsenior.manuel.model.Curso;
import com.devsenior.manuel.model.Estudiante;
import com.devsenior.manuel.model.Inscripcion;

public class InscripcionService {
    private static final Logger logger = LogManager.getLogger(InscripcionService.class);

    private List<Inscripcion> inscripciones;

    public InscripcionService() {
        logger.info("InscripcionService initialized");
        this.inscripciones = new ArrayList<>();
    }

    public boolean cursoLleno (Curso curso) {
        return curso.getInscripciones().size() >= curso.getCapacidad();
    }

    public void agregarEstudiante (Curso curso, Estudiante estudiante, LocalDate fechaInscripcion) throws CursoLlenoException {
        // Validaciones de null
        if (curso == null) {
            logger.error("Course parameter is null");
            throw new IllegalArgumentException("El curso no puede ser null");
        }
        if (estudiante == null) {
            logger.error("Student parameter is null");
            throw new IllegalArgumentException("El estudiante no puede ser null");
        }
        if (fechaInscripcion == null) {
            logger.error("Date parameter is null");
            throw new IllegalArgumentException("La fecha no puede ser null");
        }
        
        // Verificar si el estudiante ya está inscrito en este curso
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getCurso().getCodigo().equals(curso.getCodigo()) && 
                inscripcion.getEstudiante().getId().equals(estudiante.getId())) {
                logger.warn("Student " + estudiante.getNombre() + " already enrolled in course " + curso.getNombre());
                throw new EstudianteDuplicadoException("El estudiante " + estudiante.getNombre() + 
                    " ya está inscrito en el curso " + curso.getNombre());
            }
        }
        
        logger.info("Adding student: " + estudiante + " to course: " + curso);
        if(cursoLleno(curso)){
            logger.warn("Cannot add student: " + estudiante + " to course: " + curso + " - Course is full");
            throw new CursoLlenoException("Curso " + curso.getNombre() + " está lleno.");
        }
        // Creo la incripción
        Inscripcion inscripcion = new Inscripcion(curso, estudiante, fechaInscripcion);
        // Agrego la inscripción a la lista de inscripciones y al curso
        inscripciones.add(inscripcion);
        curso.agregarInscripcion(inscripcion);
        logger.info("Student: " + estudiante + " added to course: " + curso);
    }

    public void eliminarEstudiante (Curso curso, Estudiante estudiante) throws EstudianteNoEncontradoException{
        // Validaciones de null
        if (curso == null) {
            logger.error("Course parameter is null in remove operation");
            throw new IllegalArgumentException("El curso no puede ser null");
        }
        if (estudiante == null) {
            logger.error("Student parameter is null in remove operation");
            throw new IllegalArgumentException("El estudiante no puede ser null");
        }
        
        // Verificar si hay inscripciones
        if (inscripciones.isEmpty()) {
            logger.warn("Attempted to remove student from empty enrollment list");
            throw new EstudianteNoEncontradoException("No hay inscripciones registradas");
        }
        
        // Verificar si el curso tiene inscripciones
        if (curso.getInscripciones().isEmpty()) {
            logger.warn("Course " + curso.getNombre() + " has no enrollments");
            throw new EstudianteNoEncontradoException("El curso " + curso.getNombre() + " no tiene inscripciones");
        }
        
        logger.info("Removing student: " + estudiante.getNombre() + " from course: " + curso.getNombre());
        Inscripcion inscripcionAEliminar = null;
        for(Inscripcion inscripcion : inscripciones) {
            if(inscripcion.getCurso().getCodigo().equals(curso.getCodigo()) && inscripcion.getEstudiante().getId().equals(estudiante.getId())) {
                inscripcionAEliminar = inscripcion;
                break;
            }
        }
        if(inscripcionAEliminar != null) {
            inscripciones.remove(inscripcionAEliminar);
            curso.getInscripciones().remove(inscripcionAEliminar);
            logger.info("Student: " + estudiante + " removed from course: " + curso);
        } else {
            logger.warn("Student: " + estudiante + " not found in course: " + curso);
            throw new EstudianteNoEncontradoException("Estudiante " + estudiante.getNombre() + " no encontrado en el curso " + curso.getNombre());
        }
    }

    public List<Inscripcion> ListarInscripcionesPorEstudiante(Estudiante estudiante) throws EstudianteNoEncontradoException {
        if (estudiante == null) {
            logger.error("Student parameter is null in list operation");
            throw new IllegalArgumentException("El estudiante no puede ser null");
        }
        
        logger.info("Listing enrollments for student: " + estudiante);
        List<Inscripcion> resultado = new ArrayList<>();
        for(Inscripcion inscripcion : inscripciones) {
            if(inscripcion.getEstudiante().getId().equals(estudiante.getId())) {
                resultado.add(inscripcion);
            }
        }

        if(resultado.size() == 0) {
            logger.warn("No enrollments found for student: " + estudiante);
            throw new EstudianteNoEncontradoException("Estudiante no encontrado en inscripciones");
        }

        logger.info("Found " + resultado.size() + " enrollments for student: " + estudiante.getNombre());
        return resultado;
    }


}
