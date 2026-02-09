package com.devsenior.manuel.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.*;

import com.devsenior.manuel.exception.CursoDuplicadoException;
import com.devsenior.manuel.exception.CursoNoEncontradoException;
import com.devsenior.manuel.model.Curso;

public class CursoService {

    private static final Logger logger = LogManager.getLogger(CursoService.class);

    private List<Curso> cursos;


    public CursoService() {
        logger.info("CursoService initialized");
        cursos = new ArrayList<>();
    }

    public void agregarCurso(Curso curso) {
        if (curso == null) {
            logger.error("Attempted to add null course");
            throw new IllegalArgumentException("El curso no puede ser null");
        }
        
        // Verificar si ya existe un curso con el mismo código
        for (Curso c : cursos) {
            if (c.getCodigo().equals(curso.getCodigo())) {
                logger.warn("Attempted to add duplicate course with code: " + curso.getCodigo());
                throw new CursoDuplicadoException("Ya existe un curso con el código: " + curso.getCodigo());
            }
        }
        
        logger.info("Adding course: " + curso);
        cursos.add(curso);
    }

    public List<Curso> getCursos() {
        logger.info("Retrieving all courses");
        return cursos;
    }

    public Curso getCursoPorId(String codigo) {
        logger.info("Searching for course with ID: " + codigo);
        for(Curso curso : cursos) { 
            if(curso.getCodigo().equals(codigo)) {
                logger.info("Course found: " + curso);
                return curso;
            }
        }
        logger.warn("Course with ID " + codigo + " not found");
        throw new CursoNoEncontradoException("Curso con código " + codigo + " no fue encontrado.");
    }

}
