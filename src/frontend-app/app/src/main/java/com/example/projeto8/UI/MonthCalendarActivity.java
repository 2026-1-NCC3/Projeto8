package com.example.projeto8.UI;

import static com.example.projeto8.UI.CalendarUtils.daysInMonthArray;
import static com.example.projeto8.UI.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto8.R;
import com.example.projeto8.adapter.TaskAdapter;
import com.example.projeto8.model.Task;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class MonthCalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView monthYearText, selectedDateTV;
    private RecyclerView calendarRecyclerView;
    private View btnCalendar, btnHome, btnProfile;
    private View containerCalendar, containerHome, containerProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }

        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }

        initWidgets();
        setMonthView();
        setupMenuClicks();
    }


    private void initWidgets() {
        monthYearText = findViewById(R.id.monthYearTV);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        selectedDateTV = findViewById(R.id.selectedDateTV);

        // Referência segura para o ícone selecionado
        View menuInclude = findViewById(R.id.menu);
        if (menuInclude != null) {
            // Botões principais para clique
            btnCalendar = menuInclude.findViewById(R.id.btnCalendar);
            btnHome = menuInclude.findViewById(R.id.btnHome);
            btnProfile = menuInclude.findViewById(R.id.btnProfile);

            // Containers internos para o fundo rosa (use os IDs do seu XML)
            containerCalendar = menuInclude.findViewById(R.id.containerCalendarSelect);
            containerHome = menuInclude.findViewById(R.id.containerHomeSelect);
            containerProfile = menuInclude.findViewById(R.id.containerProfileSelect);

            // CONFIGURAÇÃO INICIAL: Como estamos na Agenda, ela começa selecionada
            if (btnCalendar != null) {
                btnCalendar.setSelected(true);
                if (containerCalendar != null) {
                    containerCalendar.setBackgroundResource(R.drawable.selected_item_bg);
                }
            }
        }
        updateMenuSelection(
                btnCalendar, containerCalendar,
                btnHome, containerHome,
                btnProfile, containerProfile
        );
    }

    public void setupMenuClicks() {
// Clique na Agenda (Já está nela, pode apenas resetar a view se quiser)
        btnCalendar.setOnClickListener(v -> {
            updateMenuSelection(btnCalendar, containerCalendar, btnHome, containerHome, btnProfile, containerProfile);
        });

        // Clique na Home
        btnHome.setOnClickListener(v -> {
            updateMenuSelection(btnHome, containerHome, btnCalendar, containerCalendar, btnProfile, containerProfile);
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
            finish(); // Opcional: fecha a agenda ao ir para a home
        });

        // Clique no Perfil
        btnProfile.setOnClickListener(v -> {
            updateMenuSelection(btnProfile, containerProfile, btnCalendar, containerCalendar, btnHome, containerHome);
            startActivity(new Intent(this, ProfileActivity.class));
            overridePendingTransition(0, 0);
        });
    }

    private void updateMenuSelection(View selectedBtn, View selectedContainer, View... others) {
        for (View view : others) {
            if (view != null) {
                view.setSelected(false);
                // Se for um dos containers internos, removemos o background
                if (view == containerHome || view == containerCalendar || view == containerProfile) {
                    view.setBackground(null);
                }
            }
        }

        if (selectedBtn != null) selectedBtn.setSelected(true);
        if (selectedContainer != null) {
            selectedContainer.setBackgroundResource(R.drawable.selected_item_bg);
        }
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, R.drawable.circle_selected);
        // IMPORTANTE: Usar "this" em vez de getApplicationContext() para evitar crashes de layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);


    }



    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView();
            updateSelectedDateText();
        }
    }

    private void updateSelectedDateText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", new Locale("pt", "BR"));
        String formatted = CalendarUtils.selectedDate.format(formatter);
        // Deixar a primeira letra maiúscula
        selectedDateTV.setText(formatted.substring(0, 1).toUpperCase() + formatted.substring(1));
    }


    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }
}