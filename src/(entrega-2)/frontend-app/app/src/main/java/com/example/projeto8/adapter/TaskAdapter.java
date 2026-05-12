package com.example.projeto8.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto8.R;
import com.example.projeto8.model.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<Task> tasks;
    private OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    public TaskAdapter(ArrayList<Task> tasks, OnTaskClickListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardTask;
        LinearLayout expandArea, container;
        TextView txtTitle, txtVideoLink;
        ImageView imgArrow,imgEmptyState;;

        public ViewHolder(View itemView) {
            super(itemView);
            cardTask = itemView.findViewById(R.id.cardTask);
            expandArea = itemView.findViewById(R.id.expandArea);
            txtTitle = itemView.findViewById(R.id.txtTodayE);
            container = itemView.findViewById(R.id.container);
            imgArrow = itemView.findViewById(R.id.imgArrow);
            txtVideoLink = itemView.findViewById(R.id.txtVideoLink);
            imgEmptyState = itemView.findViewById(R.id.imgEmptyState);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = tasks.get(position);

        if (task.getExerciseId() == -1L) {
            holder.txtTitle.setText(task.getTitle()); // Apenas o título da mensagem
            holder.imgArrow.setVisibility(View.GONE); // Esconde a seta
            holder.expandArea.setVisibility(View.GONE); // Garante que não expande
            holder.container.setOnClickListener(null); // Desabilita o clique de expansão
            holder.imgEmptyState.setVisibility(View.VISIBLE);

            holder.cardTask.setCardBackgroundColor(Color.TRANSPARENT); // Remove a cor laranja/branca
            holder.cardTask.setCardElevation(0f);                    // Remove a sombra (essencial!)
            holder.cardTask.setMaxCardElevation(0f);
            holder.container.setBackgroundColor(Color.TRANSPARENT);
            holder.txtTitle.setGravity(Gravity.CENTER);


        } else {
            holder.imgArrow.setVisibility(View.VISIBLE);

            String textoExibicao = task.getTitle() + " | " + task.getSerie() + "x" + task.getReps();
            holder.txtTitle.setText(textoExibicao);
            holder.imgEmptyState.setVisibility(View.GONE);
            holder.expandArea.setVisibility(task.isExpanded ? View.VISIBLE : View.GONE);
            holder.imgArrow.setRotation(task.isExpanded ? 270f : 90f);
            holder.cardTask.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.lightOrange));

            String url = task.getMidiaURL();
            if (url != null && !url.isEmpty()) {
                holder.txtVideoLink.setText("Vídeo");
                holder.txtVideoLink.setTextColor(holder.itemView.getContext().getColor(R.color.black));
                holder.txtVideoLink.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onTaskClick(task);
                    }
                });
            } else {
                holder.txtVideoLink.setText("Não há vídeo");
                holder.txtVideoLink.setTextColor(holder.itemView.getContext().getColor(android.R.color.black));
                holder.txtVideoLink.setOnClickListener(null);
            }

            holder.container.setOnClickListener(v -> {
                task.isExpanded = !task.isExpanded;
                notifyItemChanged(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
    public boolean hasRealExercises() {
        for (Task t : tasks) {
            if (t.getExerciseId() != -1L) {
                return true; // Encontrou pelo menos um exercício real
            }
        }
        return false; // Só tem a mensagem de "vazio" ou a lista está vazia
    }
}