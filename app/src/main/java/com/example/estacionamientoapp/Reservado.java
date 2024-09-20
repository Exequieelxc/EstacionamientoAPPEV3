package com.example.estacionamientoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.graphics.Color;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class Reservado extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener el lugar seleccionado del Intent
        int lugar = getIntent().getIntExtra("lugar", 0);

        // Mostrar el lugar seleccionado
        TextView lugarTextView = findViewById(R.id.textView41);
        lugarTextView.setText("Lugar seleccionado: " + lugar);

        // Generar el código QR
        ImageView qrImageView = findViewById(R.id.qrImageView);
        Bitmap qrBitmap = generarQr("Lugar: " + lugar);
        qrImageView.setImageBitmap(qrBitmap);

        // Iniciar el Chronometer
        Chronometer chronometer = findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                long hours = (elapsedMillis / 3600000) % 24;
                long minutes = (elapsedMillis / 60000) % 60;
                long seconds = (elapsedMillis / 1000) % 60;
                chronometer.setText(String.format("%02d:%02d", minutes, seconds));
            }
        });
    }
    public void cancelarReserva(View view) {
        Intent intent = new Intent(this, CancelarReserva.class);
        startActivity(intent);
    }
    private Bitmap generarQr(String contenido) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(contenido, BarcodeFormat.QR_CODE, 200, 200);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE); // Corrección aquí
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}