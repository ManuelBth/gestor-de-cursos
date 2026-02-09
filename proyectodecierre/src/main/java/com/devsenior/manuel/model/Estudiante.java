package com.devsenior.manuel.model;

public class Estudiante {

    private String nombre;
    private String id;
    private String email;

    public Estudiante(String nombre, String id, String email) {
        this.nombre = nombre;
        this.id = id;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Estudiante [nombre=" + nombre + ", id=" + id + ", email=" + email + "]";
    }   

}
