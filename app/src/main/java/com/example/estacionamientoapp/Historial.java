package com.example.estacionamientoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Historial extends BaseActivity {

    private HistorialAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Toolbar toolbar = findViewById(R.id.toolbarHistorial);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Historial");
        }

        toolbar.setNavigationOnClickListener(v -> {
        });

        String userId = getIntent().getStringExtra("userId");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewHistorial);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistorialAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        if (userId != null) {
            DatabaseReference historialRef = FirebaseDatabase.getInstance().getReference("historial").child(userId);

            historialRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<HistorialItem> historialList = new ArrayList<>();
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    if (nombre != null) {
                        historialList.add(new HistorialItem(nombre));
                    }
                    adapter.setHistorial(historialList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_estacionamientos) {
            Intent intent = new Intent(this, Principal1.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_favoritos) {
            Intent intent = new Intent(this, Favoritos.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_historial) {
            return true;
        } else if (id == R.id.action_cerrar_sesion) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
    }
}