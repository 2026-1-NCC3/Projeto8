package com.example.projeto8.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto8.R;
import com.example.projeto8.model.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private ArrayList<Task> tasks;

    public TaskAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardTask;
        LinearLayout expandArea, container;
        ImageView btnCheck;
        TextView txtTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            // Se o seu XML for um ConstraintLayout, use isso:
            cardTask = itemView.findViewById(R.id.cardTask);
            expandArea = itemView.findViewById(R.id.expandArea);
            btnCheck = itemView.findViewById(R.id.btnCheck);
            txtTitle = itemView.findViewById(R.id.txtTodayE);
            container = itemView.findViewById(R.id.container);
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

        holder.txtTitle.setText(task.getName());
        holder.expandArea.setVisibility(task.isExpanded ? View.VISIBLE : View.GONE);

        if (task.isDone) {
            // CORRETO: Apenas o seu ícone e a cor verde
            holder.container.setBackgroundResource(R.drawable.task_bg_done);
            holder.btnCheck.setImageResource(R.drawable.ic_done);
            holder.cardTask.setBackgroundColor(
                    holder.itemView.getContext().getColor(android.R.color.holo_green_light)
            );

        } else {
            // CORRETO: Apenas o seu ícone vazio e a cor branca
            holder.container.setBackgroundResource(R.drawable.task_bg);
            holder.btnCheck.setImageResource(R.drawable.ic_empty);
            holder.cardTask.setBackgroundColor(
                    holder.itemView.getContext().getColor(android.R.color.white)
            );
        }

        // Clique para expandir
        holder.cardTask.setOnClickListener(v -> {
            task.isExpanded = !task.isExpanded;
            notifyItemChanged(position);
        });

        // Clique para marcar/desmarcar
        holder.btnCheck.setOnClickListener(v -> {
            task.isDone = !task.isDone;
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
