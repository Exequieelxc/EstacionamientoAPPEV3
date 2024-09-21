package com.example.estacionamientoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class Principal1 extends AppCompatActivity {
    private Map<String, String> estacionamientos;
    private String userId;
    private static final int REQUEST_CODE_HISTORIAL = 1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_estacionamientos) {
            Toast.makeText(this, "Opción Estacionamiento seleccionada", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_favoritos) {
            Intent intent = new Intent(this, Favoritos.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_historial) {
            Log.d("Principal1", "Opción Historial seleccionada");
            Intent intent = new Intent(this, Historial.class);
            if (userId != null) {
                intent.putExtra("userId", userId);
                startActivityForResult(intent, REQUEST_CODE_HISTORIAL);
            } else {
                Log.e("Principal1", "userId es nulo al intentar abrir Historial");
            }
            return true;
        } else if (itemId == R.id.action_cerrar_sesion) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal1);

        if (savedInstanceState != null) {
            userId = savedInstanceState.getString("userId");
            Log.d("Principal1", "userId recuperado del Bundle: " + userId);
        } else {
            userId = getIntent().getStringExtra("userId");
            Log.d("Principal1", "userId recuperado del Intent: " + userId);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        estacionamientos = new HashMap<>();
        estacionamientos.put("Estacionamiento Central Copiapo", "estacionamiento1");

        ConstraintLayout contenedorEstacionamiento = findViewById(R.id.contenedorEstacionamiento);
        contenedorEstacionamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreEstacionamiento = "Estacionamiento Central Copiapo";
                String idEstacionamiento = obtenerIdEstacionamiento(nombreEstacionamiento);
                Log.d("Principal1", "userId antes de acceder a la base de datos: " + userId);

                if (userId != null) {
                    DatabaseReference historialRef = FirebaseDatabase.getInstance().getReference("historial").child(userId);
                    Map<String, Object> historialData = new HashMap<>();
                    historialData.put("nombre", nombreEstacionamiento);
                    historialData.put("capacidad", 10);
                    historialData.put("timestamp", ServerValue.TIMESTAMP);
                    historialRef.setValue(historialData);

                    if (idEstacionamiento != null) {
                        Intent intent = new Intent(Principal1.this, DetalleEstacionamiento.class);
                        intent.putExtra("nombreEstacionamiento", nombreEstacionamiento);
                        intent.putExtra("idEstacionamiento", idEstacionamiento);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Principal1.this, "Estacionamiento no encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            }
        });
    }

    private String obtenerIdEstacionamiento(String nombreEstacionamiento) {
        return estacionamientos.get(nombreEstacionamiento);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("userId", userId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_HISTORIAL && resultCode == RESULT_OK) {
            userId = data.getStringExtra("userId");
        }
    }
}