package com.example.projeto8.UI;

import static com.example.projeto8.UI.CalendarUtils.daysInWeekArray;
import static com.example.projeto8.UI.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.example.projeto8.R;
import com.example.projeto8.adapter.TaskAdapter;
import com.example.projeto8.api.appointment.AppointmentService;
import com.example.projeto8.model.Appointment;
import com.example.projeto8.model.ExerciseSession;
import com.example.projeto8.model.Task;
import com.example.projeto8.model.WorkoutSession;
import com.example.projeto8.api.workout.WorkoutService;
import com.example.projeto8.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView; // Calendário (dias)
    private TextView txtName;
    private RecyclerView recyclerTasks;
    private TaskAdapter adapter;
    private ArrayList<Task> tasksParaExibir;
    private ArrayList<Task> taskList;
    ImageView iconHome, iconCalendar, iconProfile; // Menu
    View btnHome, btnCalendar, btnProfile;
    View containerHome, containerCalendar, containerProfile;
    private CalendarAdapter calendarAdapter;
    private Button btnStartWorkout;
    private HashSet<LocalDate> globalDiasComTreino = new HashSet<>();
    private Long currentWorkoutId = -1L;
    private boolean isCurrentWorkoutChecked = false;


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("STORAGE", MODE_PRIVATE);

        String idRecebido = prefs.getString("patient_id", null);
        String nomeRecebido = prefs.getString("patient_name", null);

        if (nomeRecebido != null) {
            txtName.setText(nomeRecebido);
        }

        if (idRecebido != null) {
            WorkoutSeshData(idRecebido); // Busca os exercícios associados ao paciente
            checkAppointmentsData(UUID.fromString(idRecebido)); // Busca os agendamentos do paciente
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); // Carrega o XML principal

        // Inicializa todos os componentes do XML
        initWidgets();
        setupWorkoutActions();

        tasksParaExibir = new ArrayList<>();

        adapter = new TaskAdapter(tasksParaExibir, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                String url = task.getMidiaURL();
                if (url != null && !url.isEmpty()) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Erro ao abrir o vídeo", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        recyclerTasks.setAdapter(adapter);

        // Configurações de Menu e Calendário
        setupMenuClicks();
        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }
        setWeekView();
    }

    public void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);

        txtName = findViewById(R.id.txtName);
        recyclerTasks = findViewById(R.id.recyclerTasks);
        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));

        View menuInclude = findViewById(R.id.menu);
        if (menuInclude != null) {
            btnCalendar = menuInclude.findViewById(R.id.btnCalendar);
            btnHome = menuInclude.findViewById(R.id.btnHome);
            btnProfile = menuInclude.findViewById(R.id.btnProfile);

            containerCalendar = menuInclude.findViewById(R.id.containerCalendarSelect);
            containerHome = menuInclude.findViewById(R.id.containerHomeSelect);
            containerProfile = menuInclude.findViewById(R.id.containerProfileSelect);

            if (btnHome != null) {
                btnHome.setSelected(true);
                if (containerCalendar != null) {
                    containerCalendar.setBackgroundResource(R.drawable.selected_item_bg);
                }
            }
        }
        updateMenuSelection(
                btnHome,       // Selecionado
                containerHome, // Container Selecionado
                btnProfile, containerProfile, btnCalendar, containerCalendar // TODOS os outros que devem ser resetados
        );

        btnStartWorkout = findViewById(R.id.btnStartWorkout);
    }

    private void animateClick(View view) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale_up_down);
        view.startAnimation(anim);
    }

    public void setupMenuClicks() {

        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                animateClick(v);
                // Abre a MainActivity e fecha a Profile
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // BOTÃO CALENDÁRIO
        if (btnCalendar != null) {
            btnCalendar.setOnClickListener(v -> {
                animateClick(v);
                // Abre o Calendário e fecha a Profile
                Intent intent = new Intent(MainActivity.this, MonthCalendarActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // BOTÃO PERFIL (Onde você já está)
        if (btnHome != null) {
            btnHome.setOnClickListener(v -> {
                animateClick(v);
                // Apenas visual: atualiza a seleção sem abrir nova Activity
                updateMenuSelection(btnProfile, containerProfile, btnCalendar, containerCalendar, btnHome, containerHome);
            });
        }
    }

    public void setupWorkoutActions() {
        btnStartWorkout.setOnClickListener(v -> {
            animateClick(v);

            // Só abre se houver um treino carregado
            if (currentWorkoutId != -1L) {
                Intent intent = new Intent(MainActivity.this, ExercisesActivity.class);
                intent.putParcelableArrayListExtra("LISTA_EXERCICIOS", tasksParaExibir);
                intent.putExtra("WORKOUT_ID", currentWorkoutId);
                intent.putExtra("IS_CHECKED", isCurrentWorkoutChecked);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Nenhum treino selecionado para hoje!", Toast.LENGTH_SHORT).show();
            }
        });
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

    // Monta o calendário semanal
// Monta o calendário semanal
    private void setWeekView() {

        // "Mar 2026"
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));

        // Pega os 7 dias da semana
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);


        calendarAdapter = new CalendarAdapter(days, this, R.drawable.selected_day_bg);

        calendarAdapter.setWorkoutDates(globalDiasComTreino);

        // Define layout em grade com 7 colunas
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

            SharedPreferences prefs = getSharedPreferences("STORAGE", MODE_PRIVATE);
            String id = prefs.getString("patient_id", null);

            if (id != null) {
                WorkoutSeshData(id);
            }
        }
    }

    // Para associar o dia do exercicio com o dia da semana
    private String getDiaSemanaAbreviado(int diaSemana) {
        switch (diaSemana) {
            case 1:
                return "SEG";
            case 2:
                return "TER";
            case 3:
                return "QUA";
            case 4:
                return "QUI";
            case 5:
                return "SEX";
            case 6:
                return "SAB";
            case 7:
                return "DOM";
            default:
                return "";
        }
    }

    // Mostrar a Workout do dia
    private void WorkoutSeshData(String patientId) {
        WorkoutService api = RetrofitClient.getWorkoutService();
        api.getWorkoutsByPatient(patientId).enqueue(new Callback<List<WorkoutSession>>() {

            @Override
            public void onResponse(Call<List<WorkoutSession>> call, Response<List<WorkoutSession>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    android.util.Log.d("TESTE_API", "O servidor respondeu agora!");

                    List<WorkoutSession> listaDeTreinos = response.body();
                    // TUDO que mexe na tela precisa de permissão do Android para rodar na UI Thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tasksParaExibir.clear();
                                currentWorkoutId = -1L;

                                HashSet<LocalDate> diasComTreino = new HashSet<>();
                                // Pegamos os 7 dias que estão aparecendo no calendário agora
                                ArrayList<LocalDate> diasNaTela = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate);

                                int diaSemana = CalendarUtils.selectedDate.getDayOfWeek().getValue();
                                String diaAtual = getDiaSemanaAbreviado(diaSemana);

                                for (WorkoutSession treino : listaDeTreinos) {
                                    if (treino.getWeekDay() != null) {
                                        String diaSemanaTreino = treino.getWeekDay().trim().toUpperCase();

                                        // --- LÓGICA DA BOLINHA (Para todos os dias da semana) ---
                                        for (LocalDate data : diasNaTela) {
                                            // Pega o número do dia (1 a 7) e converte para SEG, TER, QUA...
                                            String diaDaTelaTraduzido = getDiaSemanaAbreviado(data.getDayOfWeek().getValue());

                                            // Agora sim, compara SEG com SEG
                                            if (diaDaTelaTraduzido.equals(diaSemanaTreino)) {
                                                diasComTreino.add(data);
                                            }
                                        }

                                        // --- LÓGICA DA LISTA (Apenas para o dia clicado) ---
                                        if (diaSemanaTreino.equals(diaAtual)) {
                                            isCurrentWorkoutChecked = treino.getChecked();
                                            currentWorkoutId = treino.getWorkoutSession_id();

                                            if (treino.getExercises() != null) {
                                                for (ExerciseSession session : treino.getExercises()) {
                                                    // ... seu código de adicionar na tasksParaExibir continua igual ...
                                                    tasksParaExibir.add(new Task(
                                                            session.getExercisesession_id() != null ? session.getExercisesession_id() : -1L,
                                                            session.getExercise() != null ? session.getExercise().getTitle() : "Exercício s/ nome",
                                                            session.getSerie(),
                                                            session.getRepetitions(),
                                                            session.getExercise() != null ? session.getExercise().getMidiaURL() : "",
                                                            session.getExercise() != null ? session.getExercise().getDescription() : ""
                                                    ));
                                                }
                                            }
                                        }
                                    }
                                }

                                if (tasksParaExibir.isEmpty()) {
                                    tasksParaExibir.add(
                                            new Task(-1L, "Nenhum exercício para hoje! Descanse.", 0, 0, "", "")
                                    );
                                }
                                if (recyclerTasks.getAdapter() == null) {
                                    recyclerTasks.setAdapter(adapter);
                                }
                                adapter.notifyDataSetChanged();

                                globalDiasComTreino.clear();
                                globalDiasComTreino.addAll(diasComTreino);
                                if (calendarAdapter != null) {
                                    calendarAdapter.setWorkoutDates(globalDiasComTreino);
                                }


                                if (adapter.hasRealExercises()) {
                                    btnStartWorkout.setVisibility(View.VISIBLE);
                                } else {
                                    btnStartWorkout.setVisibility(View.GONE);
                                }

                            } catch (Exception e) {
                                Log.e("TESTE_API", "Erro ao atualizar interface: " + e.getMessage());
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<WorkoutSession>> call, Throwable t) {
                Log.e("API_ERRO", "Mensagem: " + t.getMessage());
                runOnUiThread(() -> txtName.setText("ERRO DE CONEXÃO"));
            }
        });
    }

    private void checkAppointmentsData(UUID patientId) {
        AppointmentService api = RetrofitClient.getAppointmentService();
        api.getAppointmentByPatient(patientId).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Appointment appo : response.body()) {
                        try {
                            String fullDateFromApi = appo.getDate();
                            String timeFromApi = appo.getTime();

                            String cleanDate = fullDateFromApi.substring(0, 10);
                            String isoDateTime = cleanDate + "T" + timeFromApi;

                            if (timeFromApi.length() == 5) {
                                isoDateTime += ":00";
                            }

                            LocalDateTime dataEHoraReal = LocalDateTime.parse(isoDateTime);
                            NotificationScheduler.schedule(
                                    MainActivity.this,
                                    dataEHoraReal,
                                    appo.getDescription(),
                                    appo.getTime());
                        } catch (Exception e) {
                            Log.e("NOTIF_ERRO", "Falha ao processar data/hora: " + e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Log.e("API_APPOINTMENT", "Erro: " + t.getMessage());
            }
        });
    }
}

