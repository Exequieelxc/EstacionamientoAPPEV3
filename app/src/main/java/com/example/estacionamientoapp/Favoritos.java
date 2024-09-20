package com.example.estacionamientoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class Favoritos extends AppCompatActivity {

    private RecyclerView recyclerViewFavoritos;
    private EstacionamientoAdapter estacionamientoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favoritos);
        Toolbar toolbar = findViewById(R.id.toolbarFavoritos);
        setSupportActionBar(toolbar);

        recyclerViewFavoritos = findViewById(R.id.recyclerViewFavoritos);
        recyclerViewFavoritos.setLayoutManager(new LinearLayoutManager(this));
        estacionamientoAdapter = new EstacionamientoAdapter(new ArrayList<>());
        recyclerViewFavoritos.setAdapter(estacionamientoAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.coordinatorLayoutFavoritos), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cargarFavoritos();
    }

    private void cargarFavoritos() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference favoritosRef = FirebaseDatabase.getInstance().getReference("favoritos").child(userId);

            favoritosRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> favoritosIds = new ArrayList<>();
                    for (DataSnapshot favoritoSnapshot : snapshot.getChildren()) {
                        String estacionamientoId = favoritoSnapshot.getKey();
                        favoritosIds.add(estacionamientoId);
                    }

                    obtenerEstacionamientos(favoritosIds);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Favoritos", "Error al obtener favoritos", error.toException());
                }
            });
        }
    }

    private void obtenerEstacionamientos(List<String> favoritosIds) {
        DatabaseReference estacionamientosRef = FirebaseDatabase.getInstance().getReference("estacionamientos");
        List<Estacionamiento> estacionamientos = new ArrayList<>();

        for (String estacionamientoId : favoritosIds) {
            estacionamientosRef.child(estacionamientoId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nombre = snapshot.child("nombre").getValue(String.class);
                        String imagenUrl = snapshot.child("imagenUrl").getValue(String.class);
                        boolean esFavorito = false;
                        if (snapshot.child("esFavorito").exists()) {
                            esFavorito = snapshot.child("esFavorito").getValue(Boolean.class);
                        }
                        Estacionamiento estacionamiento = new Estacionamiento(estacionamientoId, nombre, imagenUrl, esFavorito);
                        estacionamientos.add(estacionamiento);

                        estacionamientoAdapter.setEstacionamientos(estacionamientos);
                        estacionamientoAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Favoritos", "Error al obtener estacionamiento", error.toException());
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.op1) {
            finish();
            return true;
        } else if (itemId == R.id.op3) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Favoritos.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public class EstacionamientoAdapter extends RecyclerView.Adapter<EstacionamientoAdapter.EstacionamientoViewHolder> {

        private List<Estacionamiento> estacionamientos;

        public EstacionamientoAdapter(List<Estacionamiento> estacionamientos) {
            this.estacionamientos = estacionamientos;
        }

        public void setEstacionamientos(List<Estacionamiento> estacionamientos) {
            this.estacionamientos = estacionamientos;
        }

        @NonNull
        @Override
        public EstacionamientoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_estacionamiento, parent, false); // Corregido a item_estacionamiento
            return new EstacionamientoViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull EstacionamientoViewHolder holder, int position) {
            Estacionamiento estacionamiento = estacionamientos.get(position);
            holder.nombreTextView.setText(estacionamiento.getNombre());


            Glide.with(holder.itemView.getContext())
                    .load(estacionamiento.getImagenUrl())
                    .into(holder.imagenImageView);


            if (estacionamiento.isEsFavorito()) {
                holder.estrellaImageView.setImageResource(R.drawable.estrella_encendida);
            } else {
                holder.estrellaImageView.setImageResource(R.drawable.estrella_apagada);
            }


            holder.estrellaImageView.setOnClickListener(v -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    String estacionamientoId = estacionamiento.getId();


                    DatabaseReference favoritosRef = FirebaseDatabase.getInstance().getReference("favoritos").child(userId).child(estacionamientoId);
                    favoritosRef.removeValue();


                    estacionamientos.remove(estacionamiento);
                    notifyItemRemoved(position);
                }
            });


            holder.itemView.setOnClickListener(view -> {
                String idEstacionamiento = estacionamiento.getId();
                Intent intent = new Intent(holder.itemView.getContext(), DetalleEstacionamiento.class);
                intent.putExtra("idEstacionamiento", idEstacionamiento);
                intent.putExtra("nombreEstacionamiento", estacionamiento.getNombre());
                holder.itemView.getContext().startActivity(intent);
            });
        }


        @Override
        public int getItemCount() {
            return estacionamientos.size();
        }


        public class EstacionamientoViewHolder extends RecyclerView.ViewHolder {


            public TextView nombreTextView;
            public ImageView imagenImageView;
            public ImageView estrellaImageView;


            public EstacionamientoViewHolder(@NonNull View itemView) {
                super(itemView);
                nombreTextView = itemView.findViewById(R.id.nombreEstacionamiento);
                imagenImageView = itemView.findViewById(R.id.imagenEstacionamiento);
                estrellaImageView = itemView.findViewById(R.id.estrellaFavorito);}
        }
    }
}