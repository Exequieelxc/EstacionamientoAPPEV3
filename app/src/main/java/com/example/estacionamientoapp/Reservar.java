package com.example.estacionamientoapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Reservar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView lugar1ImageView = findViewById(R.id.lugar1);
        ImageView lugar2ImageView = findViewById(R.id.lugar2);
        ImageView lugar3ImageView = findViewById(R.id.lugar3);
        ImageView lugar4ImageView = findViewById(R.id.lugar4);
        ImageView lugar5ImageView = findViewById(R.id.lugar5);
        ImageView lugar6ImageView = findViewById(R.id.lugar6);
        ImageView lugar7ImageView = findViewById(R.id.lugar7);
        ImageView lugar8ImageView = findViewById(R.id.lugar8);
        ImageView lugar9ImageView = findViewById(R.id.lugar9);
        ImageView lugar10ImageView = findViewById(R.id.lugar10);

        establecerEstadoLugar(lugar1ImageView, 1);
        establecerEstadoLugar(lugar2ImageView, 2);
        establecerEstadoLugar(lugar3ImageView, 3);
        establecerEstadoLugar(lugar4ImageView, 4);
        establecerEstadoLugar(lugar5ImageView, 5);
        establecerEstadoLugar(lugar6ImageView, 6);
        establecerEstadoLugar(lugar7ImageView, 7);
        establecerEstadoLugar(lugar8ImageView, 8);
        establecerEstadoLugar(lugar9ImageView, 9);
        establecerEstadoLugar(lugar10ImageView, 10);

        // Hacer que el TextView "Volver" sea clickable
        TextView textViewVolver = findViewById(R.id.textView25);
        String text = textViewVolver.getText().toString();
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                onBackPressed();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewVolver.setText(ss);
        textViewVolver.setMovementMethod(LinkMovementMethod.getInstance());
        textViewVolver.setHighlightColor(Color.TRANSPARENT);
    }

    private boolean lugar1Disponible = true;
    private boolean lugar2Disponible = false;
    private boolean lugar3Disponible = true;
    private boolean lugar4Disponible = false;
    private boolean lugar5Disponible = true;
    private boolean lugar6Disponible = false;
    private boolean lugar7Disponible = true;
    private boolean lugar8Disponible = false;
    private boolean lugar9Disponible = true;
    private boolean lugar10Disponible = false;

    private boolean obtenerEstadoLugar(int lugarId) {
        switch (lugarId) {
            case 1:
                return lugar1Disponible;
            case 2:
                return lugar2Disponible;
            case 3:
                return lugar3Disponible;
            case 4:
                return lugar4Disponible;
            case 5:
                return lugar5Disponible;
            case 6:
                return lugar6Disponible;
            case 7:
                return lugar7Disponible;
            case 8:
                return lugar8Disponible;
            case 9:
                return lugar9Disponible;
            case 10:
                return lugar10Disponible;
            default:
                return false;
        }
    }

    private void cambiarEstadoLugar(int lugarId, boolean disponible) {
        switch (lugarId) {
            case 1:
                lugar1Disponible = disponible;
                break;
            case 2:
                lugar2Disponible = disponible;
                break;
            case 3:
                lugar3Disponible = disponible;
                break;
            case 4:
                lugar4Disponible = disponible;
                break;
            case 5:
                lugar5Disponible = disponible;
                break;
            case 6:
                lugar6Disponible = disponible;
                break;
            case 7:
                lugar7Disponible = disponible;
                break;
            case 8:
                lugar8Disponible = disponible;
                break;
            case 9:
                lugar9Disponible = disponible;
                break;
            case 10:
                lugar10Disponible = disponible;
                break;
        }
    }

    public void seleccionarLugar(View view) {
        int lugarId = Integer.parseInt(view.getTag().toString());
        for (int i = 1; i <= 10; i++) {
            cambiarEstadoLugar(i, true);
        }

        cambiarEstadoLugar(lugarId, false);
        ImageView imageView = (ImageView) view;
        imageView.setImageResource(R.drawable.estacionamientocupado);
        actualizarImagenesLugares();
        TextView mensajeTextView = findViewById(R.id.textView26);
        mensajeTextView.setText("Lugar seleccionado: " + lugarId);
    }

    private void actualizarImagenesLugares() {
        for (int i = 1; i <= 10; i++) {
            ImageView imageView = findViewById(getResources().getIdentifier("lugar" + i, "id", getPackageName()));
            establecerEstadoLugar(imageView, i);
        }
    }

    public void reservarLugar(View view) {
        TextView mensajeTextView = findViewById(R.id.textView26);
        String mensaje = mensajeTextView.getText().toString();

        if (mensaje.equals("Lugar seleccionado: ")) {
            return;
        }

        int lugarId = Integer.parseInt(mensaje.substring("Lugar seleccionado: ".length()));
        for (int i = 1; i <= 10; i++) {
            cambiarEstadoLugar(i, true);
        }
        cambiarEstadoLugar(lugarId, false);
        actualizarImagenesLugares();
        mensajeTextView.setText("Lugar seleccionado: ");

        // Iniciar la Activity Reservado
        Intent intent = new Intent(Reservar.this, Reservado.class);
        intent.putExtra("lugar", lugarId);
        startActivity(intent);
    }

    public void establecerEstadoLugar(ImageView imageView, int lugarId) {
        boolean disponible = obtenerEstadoLugar(lugarId);
        if (disponible) {
            imageView.setImageResource(R.drawable.estacionamientodisponible);
        } else {
            imageView.setImageResource(R.drawable.estacionamientocupado);
        }
    }
}