package com.devsenior.manuel.model;

import java.time.LocalDate;

public class Inscripcion {

    private Curso curso;
    private Estudiante estudiante;
    private LocalDate fechaInscripcion;

    public Inscripcion(Curso curso, Estudiante estudiante, LocalDate fechaInscripcion) {
        this.curso = curso;
        this.estudiante = estudiante;
        this.fechaInscripcion = fechaInscripcion;
    }

    public Curso getCurso() {
        return curso;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    @Override
    public String toString() {
        return "Inscripcion [curso=" + curso.getCodigo() + ", estudiante=" + estudiante.getNombre()
                + ", fechaInscripcion=" + fechaInscripcion + "]";
    }   

}
