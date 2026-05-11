package com.example.projeto8.UI;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto8.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;
    private int selectedBackgroundResource;
    private HashSet<LocalDate> datesWithWorkouts = new HashSet<>();

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener, int selectedBackgroundResource)
    {
        this.days = days;
        this.onItemListener = onItemListener;
        this.selectedBackgroundResource = selectedBackgroundResource;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (days.size() <= 7) {
            // --- CASO 1: SEMANAL (Main Activity) ---
            // Ocupa a altura total disponível do RecyclerView (170dp definido no XML)
            float density = parent.getContext().getResources().getDisplayMetrics().density;
            layoutParams.height = (int) (80 * density);

        } else {
            // --- CASO 2: MENSAL ---
            // Divide a altura total do pai por 6 para caberem todas as semanas na tela
            int parentHeight = parent.getHeight();
            if (parentHeight > 0) {
                layoutParams.height = (int) (parentHeight / 6.0);
            } else {
                // Fallback caso o parent ainda não tenha sido renderizado
                layoutParams.height = (int) (60 * parent.getContext().getResources().getDisplayMetrics().density);
            }
        }

        return new CalendarViewHolder(view, onItemListener, days);
    }




    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        LocalDate date = days.get(position);
        float density = holder.itemView.getContext().getResources().getDisplayMetrics().density;
        // 1. Reset total (Limpa o que foi usado em células recicladas)
        holder.dayOfMonth.setText("");
        holder.exerciseDot.setVisibility(View.GONE);
        holder.selectionContainer.setBackgroundResource(0);
        holder.dayOfMonth.setBackgroundResource(0);
        




        if (date != null) {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

            if (days.size() <= 7) {
                // --- MODO SEMANAL ---
                holder.selectionContainer.getLayoutParams().width = (int) (45 * density);
                holder.dayName.setVisibility(View.VISIBLE);


                // Pega o nome do dia (DOM, SEG...) traduzido
                java.util.Locale localeBR = new java.util.Locale("pt", "BR");
                String nomeDia = date.getDayOfWeek()
                        .getDisplayName(java.time.format.TextStyle.SHORT, localeBR)
                        .replace(".", "")
                        .toUpperCase();

                holder.dayName.setText(nomeDia);


                // Se a data atual está no conjunto de datas com treino, mostra a bolinha
                if (datesWithWorkouts != null && datesWithWorkouts.contains(date)) {
                    holder.exerciseDot.setVisibility(View.VISIBLE);
                } else {
                    holder.exerciseDot.setVisibility(View.GONE);
                }

                // Lógica de Seleção (Cor do dia clicado)
                if (date.equals(CalendarUtils.selectedDate)) {
                    holder.selectionContainer.setBackgroundResource(selectedBackgroundResource);
                    holder.dayOfMonth.setTextColor(Color.WHITE);
                    holder.dayName.setTextColor(Color.WHITE);
                    // Opcional: Se quiser que a bolinha fique branca quando o dia estiver selecionado
                    holder.exerciseDot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.WHITE));
                } else {
                    holder.dayOfMonth.setTextColor(Color.BLACK);
                    holder.dayName.setTextColor(Color.GRAY);
                    // Volta a cor original da bolinha (ex: laranja) quando não selecionado
                    holder.exerciseDot.setBackgroundTintList(null);
                }

            } else {
                // --- MODO MENSAL ---
                holder.dayOfMonth.setBackgroundResource(0);
                holder.selectionContainer.setBackgroundResource(0);
                holder.exerciseDot.setVisibility(View.GONE);
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);

                // Forçamos o tamanho exato
                int circleSize = (int) (45 * density);
                ViewGroup.LayoutParams txtParams = holder.dayOfMonth.getLayoutParams();
                txtParams.width = circleSize;
                txtParams.height = circleSize;

                holder.dayOfMonth.setLayoutParams(txtParams);

                // ZERAR TUDO que pode entortar
                holder.dayOfMonth.setPadding(0, 0, 0, 0);
                holder.dayOfMonth.setIncludeFontPadding(false); // Remove o respiro da fonte
                holder.dayOfMonth.setGravity(Gravity.CENTER); // Centraliza o texto no círculo
                holder.dayOfMonth.setLineSpacing(0, 1); // Garante que não haja espaço extra de linha

                if (date.equals(CalendarUtils.selectedDate)) {
                    holder.dayOfMonth.setBackgroundResource(selectedBackgroundResource);
                    holder.dayOfMonth.setTextColor(Color.WHITE);
                } else {
                    holder.dayOfMonth.setBackgroundResource(0); // Garante que não tenha fundo sobrando
                    holder.dayOfMonth.setTextColor(Color.BLACK);
                }
            }
        }
    }


    public void setWorkoutDates(HashSet<LocalDate> dates) {
        this.datesWithWorkouts = dates;
        notifyDataSetChanged(); // Isso faz o calendário redesenhar e mostrar as bolinhas
    }

    @Override
    public int getItemCount()
    {
        return days.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, LocalDate date);
    }
}