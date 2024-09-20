package com.example.estacionamientoapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CancelarReserva extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cancelar_reserva);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        int colorAmarillo = ContextCompat.getColor(this, R.color.Yellow);
        ColorFilter filterAmarillo = new PorterDuffColorFilter(colorAmarillo, PorterDuff.Mode.SRC_ATOP);

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            int numStars = ratingBar1.getNumStars();
            for (int i = 0; i < numStars; i++) {
                int starId = android.content.res.Resources.getSystem().getIdentifier("star" + (i + 1), "id", "android");
                if (ratingBar1.findViewById(starId) != null) {
                    if (i < rating) {
                        stars.findDrawableByLayerId(starId).setColorFilter(filterAmarillo);
                    } else {
                        stars.findDrawableByLayerId(starId).setColorFilter(null);
                    }
                }
            }
        });

        EditText editTextReseña = findViewById(R.id.editTextReseña);
        Button buttonEnviar = findViewById(R.id.buttonEnviar);
        TextView textViewPregunta = findViewById(R.id.textViewPregunta);

        buttonEnviar.setOnClickListener(v -> {
            float puntuacion = ratingBar.getRating();
            String reseña = editTextReseña.getText().toString();
            ratingBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Yellow)));
            ratingBar.setVisibility(View.GONE);
            editTextReseña.setVisibility(View.GONE);
            buttonEnviar.setVisibility(View.GONE);
            textViewPregunta.setVisibility(View.GONE);
            TextView textViewGracias = findViewById(R.id.textView40);
            ImageView imageViewLike = findViewById(R.id.imageView12);
            textViewGracias.setVisibility(View.VISIBLE);
            imageViewLike.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(this, Principal1.class);
                startActivity(intent);
            }, 5000);
        });
    }
}