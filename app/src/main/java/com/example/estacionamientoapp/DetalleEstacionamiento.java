package com.example.estacionamientoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetalleEstacionamiento extends AppCompatActivity {

    private ImageView favoritoImageView;
    private String estacionamientoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_estacionamiento);

        TextView nombreEstacionamientoTextView = findViewById(R.id.textView14);
        String nombreEstacionamiento = getIntent().getStringExtra("nombreEstacionamiento");
        favoritoImageView = findViewById(R.id.favoritoImageView);

        if (nombreEstacionamiento != null) {
            nombreEstacionamientoTextView.setText(nombreEstacionamiento);
        } else {
            nombreEstacionamientoTextView.setText("Nombre no disponible");
        }
        estacionamientoId = getIntent().getStringExtra("idEstacionamiento");

        if (estacionamientoId != null) {
            DatabaseReference estacionamientoRef = FirebaseDatabase.getInstance().getReference("estacionamientos").child(estacionamientoId);
            estacionamientoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String capacidad = snapshot.child("capacidad").getValue(String.class);
                        String descripcion = snapshot.child("descripcion").getValue(String.class);
                        TextView capacidadTextView = findViewById(R.id.textView16);
                        capacidadTextView.setText("Capacidad: " + capacidad);
                        TextView descripcionTextView = findViewById(R.id.textView18);
                        descripcionTextView.setText(descripcion);
                    } else {
                        Log.e("DetalleEstacionamiento", "El estacionamiento no existe");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("DetalleEstacionamiento", "Error al obtener la información del estacionamiento", error.toException());
                    // Manejar el error
                }
            });

            verificarFavorito(estacionamientoId);
        } else {
            // Manejar el caso en que no se reciba el ID
            Log.e("DetalleEstacionamiento", "No se pudo identificar el ID del estacionamiento");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void verificarFavorito(String estacionamientoId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference favoritosRef = FirebaseDatabase.getInstance().getReference("favoritos").child(userId).child(estacionamientoId);

            favoritosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        favoritoImageView.setImageResource(R.drawable.estrella_encendida);
                    } else {
                        favoritoImageView.setImageResource(R.drawable.estrella_apagada);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("DetalleEstacionamiento", "Error al verificar favorito", error.toException());
                }
            });
        }
    }

    public void volver(View view) {
        finish();
    }

    public void agregarAFavoritos(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            if (estacionamientoId != null) {
                String userId = user.getUid();
                DatabaseReference favoritosRef = FirebaseDatabase.getInstance().getReference("favoritos").child(userId);


                favoritosRef.child(estacionamientoId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                // El estacionamiento ya está en favoritos, eliminarlo
                                favoritosRef.child(estacionamientoId).removeValue();
                                favoritoImageView.setImageResource(R.drawable.estrella_apagada);
                                Toast.makeText(DetalleEstacionamiento.this, "Estacionamiento eliminado de favoritos", Toast.LENGTH_SHORT).show();
                            } else {
                                // El estacionamiento no está en favoritos, agregarlo
                                favoritosRef.child(estacionamientoId).setValue(true);
                                favoritoImageView.setImageResource(R.drawable.estrella_encendida);
                                Toast.makeText(DetalleEstacionamiento.this, "Estacionamiento agregado a favoritos", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DetalleEstacionamiento.this, "Error al acceder a favoritos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
            }
        } else {
        }
    }
    public void reservar(View view) {
        Intent intent = new Intent(this, Reservar.class);
        startActivity(intent);
    }
}