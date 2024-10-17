package com.example.estacionamientoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Cuenta extends BaseActivity {

    private EditText editTextNuevaContrasena;
    private TextInputLayout textInputLayoutNuevaContrasena;
    private TextView textViewNombreUsuario;
    private TextView textViewCorreoElectronico;
    private Button buttonCambiarContrasena;
    private Button buttonCerrarSesion;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        mAuth = FirebaseAuth.getInstance();

        editTextNuevaContrasena = findViewById(R.id.editTextNuevaContrasena);
        textInputLayoutNuevaContrasena = findViewById(R.id.textInputLayoutNuevaContrasena);
        textViewNombreUsuario = findViewById(R.id.textViewNombreUsuario);
        textViewCorreoElectronico = findViewById(R.id.textViewCorreoElectronico);
        buttonCambiarContrasena = findViewById(R.id.buttonCambiarContrasena);
        buttonCerrarSesion = findViewById(R.id.buttonCerrarSesion);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String nombreUsuario = user.getDisplayName();
            String correoElectronico = user.getEmail();

            textViewNombreUsuario.setText(nombreUsuario);
            textViewCorreoElectronico.setText(correoElectronico);

            String uid = user.getUid();
            userRef = FirebaseDatabase.getInstance().getReference("usuarios").child(uid);
        }

        buttonCambiarContrasena.setOnClickListener(v -> {
            String newPassword = editTextNuevaContrasena.getText().toString().trim();

            if (TextUtils.isEmpty(newPassword) || newPassword.length() < 6) {
                textInputLayoutNuevaContrasena.setError("La contraseña debe tener al menos 6 caracteres");
                return;
            } else {
                textInputLayoutNuevaContrasena.setError(null);
            }

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                currentUser.updatePassword(newPassword)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                userRef.child("contrasena").setValue(newPassword)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(Cuenta.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(Cuenta.this, "Error al actualizar la contraseña en la base de datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Toast.makeText(Cuenta.this, "Error al actualizar la contraseña: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(Cuenta.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCerrarSesion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}