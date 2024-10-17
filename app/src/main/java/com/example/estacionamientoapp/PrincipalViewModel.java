package com.example.estacionamientoapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PrincipalViewModel extends ViewModel {
    private MutableLiveData<Float> calificacion = new MutableLiveData<>(0f);
    private Estacionamiento estacionamiento1;
    private DatabaseReference estacionamientosRef;

    public PrincipalViewModel() {
        estacionamientosRef = FirebaseDatabase.getInstance().getReference("estacionamientos");
        estacionamiento1 = new Estacionamiento("estacionamiento1", "Estacionamiento Central Copiapo", 10);
    }
    public LiveData<Float> getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion.setValue(calificacion);
    }

    public Estacionamiento getEstacionamiento1() {
        return estacionamiento1;
    }

    public DatabaseReference getEstacionamientosRef() {
        return estacionamientosRef;
    }
}