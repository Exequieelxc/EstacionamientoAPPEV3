package com.example.estacionamientoapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetalleEstacionamiento extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_estacionamiento);

        // Obtener el nombre del estacionamiento y actualizar el TextView
        TextView nombreEstacionamientoTextView = findViewById(R.id.textView14);
        String nombreEstacionamiento = getIntent().getStringExtra("nombreEstacionamiento");

        if (nombreEstacionamiento != null) {
            nombreEstacionamientoTextView.setText(nombreEstacionamiento);
        } else {
            nombreEstacionamientoTextView.setText("Nombre no disponible");
        }

        // Configurar el WindowInsetsListener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}

