package com.example.projeto8.UI;


import static com.example.projeto8.UI.CalendarUtils.daysInMonthArray;
import static com.example.projeto8.UI.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto8.R;
import com.example.projeto8.adapter.AppointmentAdapter;
import com.example.projeto8.model.Appointment;
import com.example.projeto8.remote.RetrofitClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthCalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView monthYearText, selectedDateTV;
    private RecyclerView calendarRecyclerView;
    private View btnCalendar, btnHome, btnProfile;
    private View containerCalendar, containerHome, containerProfile;
    private RecyclerView recyclerViewAppointments;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointment> allAppointments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        RetrofitClient.init(this);

        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }

        initWidgets();
        setMonthView();
        setupMenuClicks();

        fetchAppointments();
    }
    private void fetchAppointments() {
        String uuidStr = getSharedPreferences("STORAGE", MODE_PRIVATE).getString("patientId", null);
        if (uuidStr == null) return;

        UUID patientId = UUID.fromString(uuidStr);

        RetrofitClient.getAppointmentService()
                .getAppointmentByPatient(patientId)
                .enqueue(new Callback<List<Appointment>>() {
                    @Override
                    public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            allAppointments = response.body();
                            filterByDate();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Appointment>> call, Throwable t) {
                        Log.e("API_ERROR", "Erro ao conectar: " + t.getMessage());
                    }
                });
    }

    private void filterByDate() {
        List<Appointment> filteredList = new ArrayList<>();

        for (Appointment appo : allAppointments) {
            if (appo.getDate() != null) {
                LocalDate apiDate = LocalDate.parse(appo.getDate().substring(0, 10));

                if (apiDate.equals(CalendarUtils.selectedDate)) {
                    filteredList.add(appo);
                }
            }
        }

        updateUI(filteredList);
    }

    private void updateUI(List<Appointment> filteredList) {
        if (appointmentAdapter == null) {
            appointmentAdapter = new AppointmentAdapter(filteredList);
            recyclerViewAppointments.setAdapter(appointmentAdapter);
        } else {
            appointmentAdapter.setAppointments(filteredList);
        }

        updateSelectedDateText();
    }

    private void initWidgets() {
        monthYearText = findViewById(R.id.monthYearTV);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        selectedDateTV = findViewById(R.id.selectedDateTV);

        // Inicializa o RecyclerView de agendamentos
        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));

        appointmentAdapter = new AppointmentAdapter(new ArrayList<>());
        recyclerViewAppointments.setAdapter(appointmentAdapter);

        // Referência segura para o ícone selecionado
        View menuInclude = findViewById(R.id.menu);
        if (menuInclude != null) {
            btnCalendar = menuInclude.findViewById(R.id.btnCalendar);
            btnHome = menuInclude.findViewById(R.id.btnHome);
            btnProfile = menuInclude.findViewById(R.id.btnProfile);

            containerCalendar = menuInclude.findViewById(R.id.containerCalendarSelect);
            containerHome = menuInclude.findViewById(R.id.containerHomeSelect);
            containerProfile = menuInclude.findViewById(R.id.containerProfileSelect);

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

    private void animateClick(View view) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale_up_down);
        view.startAnimation(anim);
    }
    public void setupMenuClicks() {

        // Clique na Home
        btnHome.setOnClickListener(v -> {
            animateClick(v);
            updateMenuSelection(btnHome, containerHome, btnCalendar, containerCalendar, btnProfile, containerProfile);
            startActivity(new Intent(this, MainActivity.class));
            finish();

        });


        btnCalendar.setOnClickListener(v -> {
            updateMenuSelection(btnProfile, containerProfile, btnCalendar, containerCalendar, btnHome, containerHome);

        });

        btnProfile.setOnClickListener(v -> {
            animateClick(v);
            updateMenuSelection(btnHome, containerHome, btnCalendar, containerCalendar, btnProfile, containerProfile);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

    }

    private void updateMenuSelection(View selectedBtn, View selectedContainer, View... others) {
        for (View view : others) {
            if (view != null) {
                view.setSelected(false);
                if (view == containerHome || view == containerCalendar || view == containerProfile) {
                    view.setBackground(null);
                }
            }
        }

        if (selectedBtn != null) {
            selectedBtn.setSelected(true);
        }
        if (selectedContainer != null) {
            selectedContainer.setBackgroundResource(R.drawable.selected_item_bg);
        }
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, R.drawable.circle_selected);
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
