package com.example.estacionamientoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {

    private List<HistorialItem> historial;

    public HistorialAdapter(List<HistorialItem> historial) {
        this.historial = historial;
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        return new HistorialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) {
        HistorialItem item = historial.get(position);
        holder.nombreTextView.setText(item.getNombre());
    }

    @Override
    public int getItemCount() {
        return historial.size();
    }

    public void setHistorial(List<HistorialItem> historial) {
        this.historial = historial;
        notifyDataSetChanged();
    }

    public static class HistorialViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreTextView;

        public HistorialViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreEstacionamiento);
        }
    }
}