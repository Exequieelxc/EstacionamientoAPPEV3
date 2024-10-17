package com.example.estacionamientoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Principal1 extends AppCompatActivity {

    private String userId;
    private static final int REQUEST_CODE_HISTORIAL = 1;
    private PrincipalViewModel viewModel;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_estacionamientos) {
            return true;
        } else if (itemId == R.id.action_favoritos) {
            Intent intent = new Intent(this, Favoritos.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_historial) {
            Intent intent = new Intent(this, Historial.class);
            if (userId != null) {
                intent.putExtra("userId", userId);
                startActivityForResult(intent, REQUEST_CODE_HISTORIAL);
            }
            return true;
        } else if (itemId == R.id.action_cuenta) {
            Intent intent = new Intent(this, Cuenta.class);
            startActivity(intent);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal1);

        viewModel = new ViewModelProvider(this).get(PrincipalViewModel.class);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new PrincipalFragment())
                .commit();
    }
    public PrincipalViewModel getViewModel() {
        return viewModel;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_HISTORIAL && resultCode == RESULT_OK) {
            userId = data.getStringExtra("userId");
        }
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof PrincipalFragment) {
        } else {
            super.onBackPressed();
        }
    }
}