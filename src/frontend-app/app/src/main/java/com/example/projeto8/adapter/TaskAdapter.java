package com.example.projeto8.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
        TextView txtTitle;
        ImageView imgArrow;

        public ViewHolder(View itemView) {
            super(itemView);

            // Se o seu XML for um ConstraintLayout, use isso:
            cardTask = itemView.findViewById(R.id.cardTask);
            expandArea = itemView.findViewById(R.id.expandArea);
            txtTitle = itemView.findViewById(R.id.txtTodayE);
            container = itemView.findViewById(R.id.container);
            imgArrow = itemView.findViewById(R.id.imgArrow);
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

// 1. Texto do título
        String textoExibicao = task.getTitle() + " | " + task.getSerie() + "x" + task.getReps();
        holder.txtTitle.setText(textoExibicao);

        // 2. Controle de visibilidade da expansão
        holder.expandArea.setVisibility(task.isExpanded ? View.VISIBLE : View.GONE);

        // 3. Clique para expandir (Diferente do clique de ir para outra tela)
        holder.container.setOnClickListener(v -> {
            // Inverte o estado de expansão
            task.isExpanded = !task.isExpanded;

            // Notifica o sistema para redesenhar esse item específico
            notifyItemChanged(position);
        });


        if (task.isExpanded) {
            holder.imgArrow.setRotation(270f);
        } else {
            holder.imgArrow.setRotation(90f);
        }
        holder.container.setOnClickListener(v -> {
            task.isExpanded = !task.isExpanded;

            // O notifyItemChanged fará o onBindViewHolder rodar de novo
            // e aplicar a rotação correta
            notifyItemChanged(position);
        });

        // 4. Clique no texto do vídeo (Se quiser abrir a ExercisesActivity por aqui)
        View txtVideo = holder.itemView.findViewById(R.id.txtVideoLink);
        txtVideo.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskClick(task);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
