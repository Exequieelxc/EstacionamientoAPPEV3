package com.example.estacionamientoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View v) {
        try {
            EditText campoCorreo = findViewById(R.id.correo);
            EditText campoContrasenia = findViewById(R.id.contrasenia);

            String correo = campoCorreo.getText().toString();
            String contrasenia = campoContrasenia.getText().toString();

            if (validarCredenciales(correo, contrasenia)) {
                // Iniciar sesión
                Intent i = new Intent(this, Principal1.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private boolean validarCredenciales(String correo, String contrasenia) {
        // Simular validación:
        if (correo.equals("e1") && contrasenia.equals("123")) {
            return true;
        } else {
            return false;
        }
    }

    public void registrar(View v) {
        Intent i = new Intent(this, RegistrarCuenta.class);
        startActivity(i);
    }
}