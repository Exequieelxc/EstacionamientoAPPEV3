package com.example.estacionamientoapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.HashMap;
import java.util.Map;

public class CancelarReserva extends AppCompatActivity {

    private String userId;

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
        PorterDuffColorFilter filterAmarillo = new PorterDuffColorFilter(colorAmarillo, PorterDuff.Mode.SRC_ATOP);

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }

        buttonEnviar.setOnClickListener(v -> {
            float calificacion = ratingBar.getRating();
            String comentario = editTextReseña.getText().toString();
            Log.d("CancelarReserva", "Calificación seleccionada: " + calificacion);

            if (calificacion > 0) {
                // Cambios de visibilidad y mensaje
                ratingBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Yellow)));
                ratingBar.setVisibility(View.GONE);
                editTextReseña.setVisibility(View.GONE);
                buttonEnviar.setVisibility(View.GONE);
                textViewPregunta.setVisibility(View.GONE);
                TextView textViewGracias = findViewById(R.id.textView40);
                ImageView imageViewLike = findViewById(R.id.imageView12);
                textViewGracias.setVisibility(View.VISIBLE);
                imageViewLike.setVisibility(View.VISIBLE);
                Intent intent = new Intent("RESERVA_CANCELADA");
                sendBroadcast(intent);

                DatabaseReference estacionamientosRef = FirebaseDatabase.getInstance().getReference("estacionamientos").child("estacionamiento1");
                estacionamientosRef.child("calificacion").setValue(calificacion).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("CancelarReserva", "Calificación actualizada correctamente a: " + calificacion);
                    } else {
                        Log.e("CancelarReserva", "Error al actualizar la calificación: " + task.getException());
                    }
                });

                DatabaseReference reseñasRef = FirebaseDatabase.getInstance().getReference("reseñas").child("estacionamiento1");
                String reseñaId = reseñasRef.push().getKey();

                Map<String, Object> childUpdatesReseñas = new HashMap<>();
                childUpdatesReseñas.put("calificacion", calificacion);
                childUpdatesReseñas.put("comentario", comentario);
                childUpdatesReseñas.put("usuario", userId);
                reseñasRef.child(reseñaId).setValue(childUpdatesReseñas);

                new Handler().postDelayed(() -> {
                    Intent intent1 = new Intent(this, Principal1.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    finish();
                }, 1000);
            } else {
                Log.d("CancelarReserva", "No se puede guardar la calificación, valor es 0");
            }
        });
    }
}
