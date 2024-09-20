package com.example.estacionamientoapp;

public class Estacionamiento {
    public String id;
    public String nombre;
    public String capacidad;
    public String descripcion;
    private String imagenUrl; // Nueva propiedad para la URL de la imagen
    private boolean esFavorito;

    public Estacionamiento() {}

    public Estacionamiento(String id, String nombre, String imagenUrl, boolean esFavorito) {
        this.id = id;
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
        this.esFavorito = esFavorito;
        this.capacidad = "";
        this.descripcion = "";
    }

    public Estacionamiento(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = "";
        this.descripcion = "";
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public boolean isEsFavorito() {
        return esFavorito;
    }

    public void setEsFavorito(boolean esFavorito) {
        this.esFavorito = esFavorito;
    }

}