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

    private boolean[] lugaresDisponibles = {true, true, true, true, true, true, true, true, true, true, true};
    private int lugarSeleccionado = 0;

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
        for (int i = 1; i <= 10; i++) {
            ImageView imageView = findViewById(getResources().getIdentifier("lugar" + i, "id", getPackageName()));
            establecerEstadoLugar(imageView, i);
            final int lugarId = i;
            imageView.setOnClickListener(view -> seleccionarLugar(lugarId));
        }
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

    private void seleccionarLugar(int lugarId) {
        try {
            if (!lugaresDisponibles[lugarId - 1]) {
                return;
            }
            if (lugarSeleccionado != 0) {
                lugaresDisponibles[lugarSeleccionado - 1] = true;
            }
            lugarSeleccionado = lugarId;
            lugaresDisponibles[lugarId - 1] = false;
            actualizarImagenesLugares();
            TextView mensajeTextView = findViewById(R.id.textView26);
            mensajeTextView.setText("Lugar seleccionado: " + lugarId);

        } catch (IndexOutOfBoundsException e) {
            TextView mensajeTextView = findViewById(R.id.textView26);
            mensajeTextView.setText("Error: índice fuera de rango");
        }
    }

    private void actualizarImagenesLugares() {
        for (int i = 1; i <= 10; i++) {
            ImageView imageView = findViewById(getResources().getIdentifier("lugar" + i, "id", getPackageName()));
            establecerEstadoLugar(imageView, i);
        }
    }

    public void reservarLugar(View view) {
        if (lugarSeleccionado == 0) {
            return;
        }
        Intent intent = new Intent(Reservar.this, Reservado.class);
        intent.putExtra("lugar", lugarSeleccionado);
        startActivity(intent);
        finish();
    }

    public void establecerEstadoLugar(ImageView imageView, int lugarId) {
        boolean disponible = lugaresDisponibles[lugarId - 1];
        if (disponible) {
            imageView.setImageResource(R.drawable.estacionamientodisponible);
        } else {
            imageView.setImageResource(R.drawable.estacionamientocupado);
        }
    }
}