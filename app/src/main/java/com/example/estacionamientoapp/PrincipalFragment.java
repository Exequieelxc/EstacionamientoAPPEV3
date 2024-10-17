package com.example.estacionamientoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrincipalFragment extends Fragment {

    private List<Estacionamiento> estacionamientos;
    private String userId;
    private RatingBar ratingBar;
    private PrincipalViewModel viewModel;
    private DatabaseReference estacionamientosRef;
    private Estacionamiento estacionamiento1;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("RESERVA_CANCELADA")) {
            }
        }
    };

    public void agregarReseña(float calificacion) {
        Reseña nuevaReseña = new Reseña(estacionamiento1.getId(), calificacion);
        DatabaseReference reseñasRef = FirebaseDatabase.getInstance().getReference("reseñas").child(estacionamiento1.getId());
        reseñasRef.setValue(nuevaReseña).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("PrincipalFragment", "Reseña guardada correctamente");
            } else {
                Log.e("PrincipalFragment", "Error al guardar la reseña", task.getException());
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(PrincipalViewModel.class);
        estacionamientosRef = FirebaseDatabase.getInstance().getReference("estacionamientos");
        estacionamiento1 = new Estacionamiento("estacionamiento1", "Estacionamiento Central Copiapo", 10);
        estacionamientos = new ArrayList<>();
        estacionamientos.add(estacionamiento1);

        ratingBar = view.findViewById(R.id.ratingBar);

        viewModel.getCalificacion().observe(getViewLifecycleOwner(), calificacion -> {
            ratingBar.setRating(calificacion);
        });

        DatabaseReference calificacionRef = estacionamientosRef.child(estacionamiento1.getId()).child("calificacion");
        calificacionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Float calificacion = snapshot.getValue(Float.class);
                    if (calificacion != null) {
                        viewModel.setCalificacion(calificacion);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PrincipalFragment", "Error al obtener la calificación: " + error.getMessage());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("RESERVA_CANCELADA");
        requireActivity().registerReceiver(broadcastReceiver, intentFilter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }

        if (viewModel.getCalificacion().getValue() == 0f) {
            estacionamientosRef.child(estacionamiento1.getId()).setValue(estacionamiento1)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("PrincipalFragment", "Datos del estacionamiento guardados correctamente");
                            } else {
                                Log.e("PrincipalFragment", "Error al guardar los datos del estacionamiento", task.getException());
                            }
                        }
                    });
        }

        ConstraintLayout contenedorEstacionamiento = requireView().findViewById(R.id.contenedorEstacionamiento);
        contenedorEstacionamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Estacionamiento estacionamiento = estacionamientos.get(0);
                String nombreEstacionamiento = estacionamiento.getNombre();
                String idEstacionamiento = estacionamiento.getId();
                if (userId != null) {
                    DatabaseReference historialRef = FirebaseDatabase.getInstance().getReference("historial").child(userId);
                    Map<String, Object> historialData = new HashMap<>();
                    historialData.put("nombre", nombreEstacionamiento);
                    historialData.put("capacidad", estacionamiento.getCapacidad());
                    historialData.put("timestamp", ServerValue.TIMESTAMP);
                    historialRef.setValue(historialData);

                    if (idEstacionamiento != null) {
                        Intent intent = new Intent(getActivity(), DetalleEstacionamiento.class);
                        intent.putExtra("nombreEstacionamiento", nombreEstacionamiento);
                        intent.putExtra("idEstacionamiento", idEstacionamiento);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(broadcastReceiver);
    }
}
