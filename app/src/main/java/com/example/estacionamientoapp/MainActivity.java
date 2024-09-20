package com.example.estacionamientoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
    }

    public void login(View v) {
        EditText campoCorreo = findViewById(R.id.correo);
        EditText campoContrasenia = findViewById(R.id.contrasenia);

        String correo = campoCorreo.getText().toString();
        String contrasenia = campoContrasenia.getText().toString();

        if (correo.isEmpty() || contrasenia.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese su correo electrónico y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(correo, contrasenia)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        Log.d("MainActivity", "Usuario autenticado: " + user); // Log para verificar la autenticación
                        if (user != null) {
                            String uid = user.getUid();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference usersRef = database.getReference("usuarios").child(uid);
                            usersRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                                        String nombre = usuario.getNombre();
                                        String apellido = usuario.getApellido();
                                        String correo = usuario.getCorreo();
                                        Intent i = new Intent(MainActivity.this, Principal1.class);
                                        i.putExtra("userId", user.getUid()); // Pasar el ID del usuario en el Intent
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(MainActivity.this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Error al obtener datos del usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("MainActivity", "Error al obtener datos del usuario", e);
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "Error en el inicio de sesión: Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error en el inicio de sesión: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("MainActivity", "Error en el inicio de sesión", e);
                    }
                });
    }

    public void registrar(View v) {
        Intent i = new Intent(this, RegistrarCuenta.class);
        startActivity(i);
    }
}