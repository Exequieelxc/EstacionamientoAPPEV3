package com.example.estacionamientoapp;

public class Reseña {
    private String idEstacionamiento;
    private float calificacion;

    public Reseña(String idEstacionamiento, float calificacion) {
        this.idEstacionamiento = idEstacionamiento;
        this.calificacion = calificacion;
    }

    public String getIdEstacionamiento() {
        return idEstacionamiento;
    }

    public float getCalificacion() {
        return calificacion;
    }
}
