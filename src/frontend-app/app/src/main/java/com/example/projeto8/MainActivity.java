package com.example.projeto8;

import static com.example.projeto8.CalendarUtils.daysInWeekArray;
import static com.example.projeto8.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView monthYearText; // texto "Feb 2026"
    private RecyclerView calendarRecyclerView; // calendário (dias)


    private ImageView iconHome, iconExercise, iconProfile; // menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); // carrega o XML principal

        // LIGA OS ELEMENTOS DO XML COM O JAVA
        initWidgets();

        // menu
        iconHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        iconExercise.setOnClickListener(v -> {
            startActivity(new Intent(this, ExercisesActivity.class));
        });

        iconProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

        // define a data atual
        CalendarUtils.selectedDate = LocalDate.now();

        // monta o calendário
        setWeekView();
    }
     // código fica mais limpo, initWidgets() = preparar os atores
    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);

        iconHome = findViewById(R.id.iconHome);
        iconExercise = findViewById(R.id.iconExercise);
        iconProfile = findViewById(R.id.iconProfile);
    }

    // monta o calendário semanal
    private void setWeekView() {

        // coloca "Mar 2026"
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));

        // pega os 7 dias da semana
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        // cria o adapter (responsável por desenhar cada dia)
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);

        // define layout em grade com 7 colunas
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 7);

        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    // BOTÃO VOLTAR SEMANA
    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    // BOTÃO AVANÇAR SEMANA
    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    // QUANDO CLICA EM UM DIA
    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            setWeekView();
        }
    }

}