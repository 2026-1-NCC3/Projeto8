package com.example.projeto8.UI;


import static com.example.projeto8.UI.CalendarUtils.daysInMonthArray;
import static com.example.projeto8.UI.CalendarUtils.monthYearFromDate;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

    private Button btnCancelarAgendamento;
    private TextView monthYearText, selectedDateTV;
    private RecyclerView calendarRecyclerView;
    private View btnCalendar, btnHome, btnProfile;
    private View containerCalendar, containerHome, containerProfile;
    private RecyclerView recyclerViewAppointments;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointment> allAppointments = new ArrayList<>();

    //Para pedir permissão de notificação
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d("NOTIF_PERM", "Permissão concedida pelo usuário");
                } else {
                    Toast.makeText(this, "Avisos de consulta desativados", Toast.LENGTH_SHORT).show();
                }
            });

    private static final String CHANNEL_ID = "agendamento_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        RetrofitClient.init(this);
        createNotificationChannel();
        checkNotificationPermission();

        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }

        initWidgets();
        setMonthView();
        setupMenuClicks();
        setUpAppointment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAppointments();
    }

    private void fetchAppointments() {
        String uuidStr = getSharedPreferences("STORAGE", MODE_PRIVATE).getString("patient_id", null);
        if (uuidStr == null) return;

        UUID patient_id = UUID.fromString(uuidStr);

        RetrofitClient.getAppointmentService()
                .getAppointmentByPatient(patient_id)
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

        btnCancelarAgendamento = findViewById(R.id.btnCancelarAgendamento);

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
                btnCalendar,       // Selecionado
                containerCalendar, // Container Selecionado
                btnHome, containerHome, btnProfile, containerProfile // TODOS os outros que devem ser resetados
        );
    }

    private void animateClick(View view) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale_up_down);
        view.startAnimation(anim);
    }

    public void setupMenuClicks() {
        // BOTÃO HOME
        if (btnHome != null) {
            btnHome.setOnClickListener(v -> {
                animateClick(v);
                // Abre a MainActivity e fecha a Profile
                Intent intent = new Intent(MonthCalendarActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // BOTÃO CALENDÁRIO
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                animateClick(v);
                // Abre o Calendário e fecha a Profile
                Intent intent = new Intent(MonthCalendarActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // BOTÃO PERFIL (Onde você já está)
        if (btnCalendar != null) {
            btnCalendar.setOnClickListener(v -> {
                animateClick(v);
                // Apenas visual: atualiza a seleção sem abrir nova Activity
                updateMenuSelection(btnProfile, containerProfile, btnCalendar, containerCalendar, btnHome, containerHome);
            });
        }
    }

    private void updateMenuSelection(View selectedBtn, View selectedContainer, View... others) {
        // 1. Limpa o estado de todos os outros botões e containers passados
        for (View view : others) {
            if (view != null) {
                view.setSelected(false);
                // Em vez de comparar com variáveis globais, limpamos o background
                // de qualquer View que for passada nesta lista de "others"
                view.setBackground(null);
            }
        }

        // 2. Ativa o botão selecionado
        if (selectedBtn != null) {
            selectedBtn.setSelected(true);
        }

        // 3. Aplica o fundo apenas no container selecionado
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
            filterByDate();
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

    private void checkNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {

                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notificações de Agendamento",
                    NotificationManager.IMPORTANCE_HIGH // IMPORTANCE_HIGH para aparecer o banner no topo
            );
            channel.setDescription("Canal para avisos de sessões de fisioterapia");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void setUpAppointment() {
        btnCancelarAgendamento.setOnClickListener(v -> {

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(v.getContext());

            builder.setTitle("Falar com a Maya");
            builder.setMessage("Você será redirecionado para o WhatsApp da Maia, deseja continuar?");

            // 3. Botão de "Continuar" (Aqui vai o seu código do WhatsApp)
            builder.setPositiveButton("Continuar", (dialog, which) -> {
                String whatsappUrl = "https://api.whatsapp.com/send/?phone=5511998820868&text=Olá%21%0D+gostaria+de+falar+sobre+meu+agendamento.&type=phone_number&app_absent=0";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(whatsappUrl));
                startActivity(intent);
            });


            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
            });

            builder.show();
        });
    }

}
