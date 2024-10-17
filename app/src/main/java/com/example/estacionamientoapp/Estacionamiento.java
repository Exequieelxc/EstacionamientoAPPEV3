package com.example.estacionamientoapp;

public class Estacionamiento {
    public String id;
    public String nombre;
    public int capacidad;
    public int espaciosDisponibles;
    private float calificacion;

    public Estacionamiento() {}

    public Estacionamiento(String id, String nombre, int capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.espaciosDisponibles = capacidad;
    }

    public Estacionamiento(String id, String nombre, String imagenUrl, boolean esFavorito) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = 0;
        this.espaciosDisponibles = 0;
    }

    public Estacionamiento(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = 0;
        this.espaciosDisponibles = 0;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public int getEspaciosDisponibles() {
        return espaciosDisponibles;
    }

    public void setEspaciosDisponibles(int espaciosDisponibles) {
        this.espaciosDisponibles = espaciosDisponibles;
    }
}
