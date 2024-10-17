package com.example.estacionamientoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
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

        mAuth.signInWithEmailAndPassword(correo, contrasenia)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Inicio de sesion exitoso");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            Intent intent = new Intent(MainActivity.this, Principal1.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Log.w(TAG, "Usuario no encontrado", task.getException());
                            Toast.makeText(MainActivity.this, "Correo o contraseña incorrecta",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void registrar(View v) {
        Intent i = new Intent(this, RegistrarCuenta.class);
        startActivity(i);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
        } else {
        }
    }
}