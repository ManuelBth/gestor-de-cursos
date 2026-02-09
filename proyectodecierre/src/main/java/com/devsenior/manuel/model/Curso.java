package com.devsenior.manuel.model;

import java.util.ArrayList;
import java.util.List;

public class Curso {

    private String nombre;
    private String codigo;
    private int capacidad;
    private List<Inscripcion> inscripciones;

    public Curso(String nombre, String codigo, int capacidad) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.inscripciones = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public void agregarInscripcion(Inscripcion inscripcion) {
        this.inscripciones.add(inscripcion);
    }

    @Override
    public String toString() {
        return "Curso [nombre=" + nombre + ", codigo=" + codigo + ", capacidad=" + capacidad + ", Número de inscripciones="
                + inscripciones.size() + "]";
    }
}
