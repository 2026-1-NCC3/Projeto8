package com.example.projeto8.adapter;

import android.util.Log;
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
        TextView txtTitle, txtVideoLink;
        ImageView imgArrow;

        public ViewHolder(View itemView) {
            super(itemView);

            // Se o seu XML for um ConstraintLayout, use isso:
            cardTask = itemView.findViewById(R.id.cardTask);
            expandArea = itemView.findViewById(R.id.expandArea);
            txtTitle = itemView.findViewById(R.id.txtTodayE);
            container = itemView.findViewById(R.id.container);
            imgArrow = itemView.findViewById(R.id.imgArrow);
            txtVideoLink = itemView.findViewById(R.id.txtVideoLink);
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


        String textoExibicao = task.getTitle() + " | " + task.getSerie() + "x" + task.getReps();
        holder.txtTitle.setText(textoExibicao);

        holder.expandArea.setVisibility(task.isExpanded ? View.VISIBLE : View.GONE);
        holder.imgArrow.setRotation(task.isExpanded ? 270f : 90f);

        String url = task.getMidiaURL();
        if (url != null && !url.isEmpty()) {
            holder.txtVideoLink.setText("Vídeo");
            holder.txtVideoLink.setTextColor(holder.itemView.getContext().getColor(R.color.black));
            holder.txtVideoLink.setOnClickListener(v -> {
                if (listener != null) listener.onTaskClick(task);
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


    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
