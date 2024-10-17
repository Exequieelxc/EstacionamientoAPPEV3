package com.example.estacionamientoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.estacionamientoapp.databinding.ActivityRegistrarCuentaBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarCuenta extends AppCompatActivity {

    private ActivityRegistrarCuentaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityRegistrarCuentaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView volverInicioSesion = findViewById(R.id.volverInicioSesion);
        volverInicioSesion.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrarCuenta.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void registrarse(View view) {
        String nombre = binding.editTextTextNombre.getText().toString().trim();
        String apellido = binding.editTextTextApellido.getText().toString().trim();
        String correo = binding.editTextTextEmailAddress.getText().toString().trim();
        String contrasena = binding.editTextTextPassword.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!correo.endsWith("@gmail.com")) {
            Toast.makeText(this, "El correo electrónico debe terminar en @gmail.com", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contrasena.length() < 6) {
            Toast.makeText(this, "Debe ingresar 6 dígitos mínimo en contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user != null) {
                                String uid = user.getUid();
                                Usuario usuario = new Usuario(nombre, apellido, correo, contrasena);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference usersRef = database.getReference("usuarios");
                                usersRef.child(uid).setValue(usuario)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(RegistrarCuenta.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegistrarCuenta.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(RegistrarCuenta.this, "Error al guardar datos del usuario: " + e.getMessage(), Toast.LENGTH_LONG).show()
                                        );
                            } else {
                                Toast.makeText(RegistrarCuenta.this, "Error: el usuario no está autenticado", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                Toast.makeText(RegistrarCuenta.this, "Error al registrar el usuario: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}