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


    private Map<String, String> estacionamientos; // Mapa para almacenar los estacionamientos
    private String userId;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();


        if (itemId == R.id.op1) {
            // Acción para la opción "Estacionamiento" (por ejemplo, mostrar un mensaje)
            Toast.makeText(this, "Opción Estacionamiento seleccionada", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.op2) {
            // Acción para la opción "Favoritos"
            Intent intent = new Intent(this, Favoritos.class);
            intent.putExtra("userId", userId); // Pasar el userId al Intent
            startActivity(intent);
            return true;
        } else if (itemId == R.id.historial) {
            Intent intent = new Intent(this, Historial.class);
            // Pasar el ID del usuario al Intent
            if (userId != null) {
                intent.putExtra("userId", userId);
                startActivity(intent);
            } else {
                Log.e("Principal1", "userId es nulo al intentar abrir Historial");
                Toast.makeText(this, "Error: No se pudo obtener el ID del usuario", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (itemId == R.id.op3) {
            // Acción para la opción "Cerrar Sesión"
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Cierra la Activity actual
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu); // Infla el menú desde menu1.xml
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
            // Obtener el ID del usuario del Intent solo si savedInstanceState es nulo
            userId = getIntent().getStringExtra("userId");
            Log.d("Principal1", "userId recuperado del Intent: " + userId);
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Inicializar el mapa de estacionamientos
        estacionamientos = new HashMap<>();
        estacionamientos.put("Estacionamiento Central Copiapo", "estacionamiento1");
        // ... agregar más estacionamientos


        ConstraintLayout contenedorEstacionamiento = findViewById(R.id.contenedorEstacionamiento);
        contenedorEstacionamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreEstacionamiento = "Estacionamiento Central Copiapo"; // Obtener el nombre del estacionamiento
                String idEstacionamiento = obtenerIdEstacionamiento(nombreEstacionamiento);
                Log.d("Principal1", "userId antes de acceder a la base de datos: " + userId);


                if (userId != null) {
                    // Guardar el historial en Firebase
                    DatabaseReference historialRef = FirebaseDatabase.getInstance().getReference("historial").child(userId);
                    Map<String, Object> historialData = new HashMap<>();
                    historialData.put("nombre", nombreEstacionamiento);
                    historialData.put("imagenUrl", obtenerImagenUrl(nombreEstacionamiento));
                    historialData.put("timestamp", ServerValue.TIMESTAMP);
                    historialRef.setValue(historialData);


                    if (idEstacionamiento != null) {
                        Intent intent = new Intent(Principal1.this, DetalleEstacionamiento.class);
                        intent.putExtra("nombreEstacionamiento", nombreEstacionamiento);
                        intent.putExtra("idEstacionamiento", idEstacionamiento);
                        // Pasar el ID del usuario al Intent
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Principal1.this, "Estacionamiento no encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Principal1", "userId es nulo");
                    Toast.makeText(Principal1.this, "Error: No se pudo obtener el ID del usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private String obtenerImagenUrl(String nombreEstacionamiento) {
        // Aquí debes implementar la lógica para obtener la URL de la imagen
        // a partir del nombre del estacionamiento.


        // Ejemplo: si tienes un mapa con las URLs de las imágenes
        Map<String, String> imagenesEstacionamientos = new HashMap<>();
        imagenesEstacionamientos.put("Estacionamiento Central Copiapo", "url_de_la_imagen_del_estacionamiento_central"); // Asegúrate de que esta URL sea válida
        // ... agregar más estacionamientos y sus URLs de imágenes


        return imagenesEstacionamientos.get(nombreEstacionamiento);
    }


    // Método para obtener el ID del estacionamiento a partir del nombre
    private String obtenerIdEstacionamiento(String nombreEstacionamiento) {
        return estacionamientos.get(nombreEstacionamiento);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Principal1", "userId guardado en el Bundle: " + userId);
        outState.putString("userId", userId);
    }
}