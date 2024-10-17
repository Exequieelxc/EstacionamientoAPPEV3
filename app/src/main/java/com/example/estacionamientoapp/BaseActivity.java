package com.example.estacionamientoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        String currentActivity = getClass().getSimpleName();

        if (itemId == R.id.action_cuenta) {
            if (!currentActivity.equals("Cuenta")) {
                Intent intent = new Intent(this, Cuenta.class);
                startActivity(intent);
            }
            return true;
        }
        if (itemId == R.id.action_estacionamientos) {
            if (!currentActivity.equals("Principal1") &&
                    !(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof PrincipalFragment)) {
                Intent intent = new Intent(this, Principal1.class);
                startActivity(intent);
            }
            return true;
        } else if (itemId == R.id.action_favoritos) {
            if (!currentActivity.equals("Favoritos")) {
                Intent intent = new Intent(this, Favoritos.class);
                startActivity(intent);
            }
            return true;
        } else if (itemId == R.id.action_historial) {
            if (!currentActivity.equals("Historial")) {
                Intent intent = new Intent(this, Historial.class);
                startActivity(intent);
            }
            return true;
        } else if (itemId == R.id.action_cerrar_sesion) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base);
    }
}