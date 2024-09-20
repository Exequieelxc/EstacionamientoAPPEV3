package com.example.estacionamientoapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class Historial extends AppCompatActivity {

    private HistorialAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        // Obtener el ID del usuario del Intent
        String userId = getIntent().getStringExtra("userId");

        // Obtener la referencia al RecyclerView
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView recyclerView = findViewById(R.id.recyclerViewHistorial);

        // Configurar el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistorialAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        DatabaseReference historialRef = FirebaseDatabase.getInstance().getReference("historial").child(userId);

        historialRef.orderByChild("timestamp").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<HistorialItem> historialList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String nombre = dataSnapshot.child("nombre").getValue(String.class);
                    String imagenUrl = dataSnapshot.child("imagenUrl").getValue(String.class);
                    historialList.add(new HistorialItem(nombre, imagenUrl));
                }
                adapter.setHistorial(historialList);
            }

            @OptIn(markerClass = UnstableApi.class) @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Historial", "Error al obtener el historial", error.toException());
                Toast.makeText(Historial.this, "Error al obtener el historial", Toast.LENGTH_SHORT).show();
            }
        });
    }
}