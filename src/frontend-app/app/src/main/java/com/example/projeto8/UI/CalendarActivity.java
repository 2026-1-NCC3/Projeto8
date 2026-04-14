package com.example.projeto8.UI;

import static com.example.projeto8.UI.CalendarUtils.daysInMonthArray;
import static com.example.projeto8.UI.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto8.R;
import com.example.projeto8.adapter.TaskAdapter;
import com.example.projeto8.model.ExerciseSession;
import com.example.projeto8.model.Task;
import com.example.projeto8.model.WorkoutSession;
import com.example.projeto8.remote.RetrofitClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private RecyclerView recyclerTasks;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> tasksParaExibir;
    ImageView iconHome, iconCalendar, iconProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar); // Certifique-se de criar este XML

        initWidgets();
        setupTaskRecyclerView();

        // Inicializa o calendário com a data atual (HOJE)
        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }
        iconCalendar.setSelected(true);
        setMonthView();
        setupMenuClicks();
    }

    private void initWidgets() {
        monthYearText = findViewById(R.id.monthYearTV);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        recyclerTasks = findViewById(R.id.recyclerTasks);

        iconHome = findViewById(R.id.iconHome);
        iconCalendar = findViewById(R.id.iconCalendar);
        iconProfile = findViewById(R.id.iconProfile);
    }

    private void setupTaskRecyclerView() {
        tasksParaExibir = new ArrayList<>();
        // Reutilizamos o TaskAdapter que você já tem
        taskAdapter = new TaskAdapter(tasksParaExibir, task -> {
            Intent intent = new Intent(CalendarActivity.this, ExercisesActivity.class);
            intent.putExtra("EXERCISE_TITLE", task.getTitle());
            intent.putExtra("EXERCISE_MEDIA_URL", task.getMidiaURL());
            intent.putExtra("EXERCISE_DESC", task.getDescription());
            startActivity(intent);
        });
        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerTasks.setAdapter(taskAdapter);
    }

    private void setMonthView() {
        // Define o texto do topo (ex: Abril 2026)
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));

        // Gera a lista de dias do mês (incluindo espaços vazios para alinhar a semana)
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        // Layout de Grid com 7 colunas (uma para cada dia da semana)
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        // Sempre que o mês muda ou abre, busca os treinos do dia selecionado
        loadWorkoutsForSelectedDate();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarUtils.selectedDate = date; // Atualiza a data global
            setMonthView(); // Re-renderiza para mostrar o destaque no dia clicado
        }
    }

    private void loadWorkoutsForSelectedDate() {
        SharedPreferences prefs = getSharedPreferences("STORAGE", MODE_PRIVATE);
        String id = prefs.getString("patient_id", null);

        if (id != null) {
            fetchWorkouts(id);
        }
    }

    private void fetchWorkouts(String patientId) {
        RetrofitClient.getWorkoutService().getWorkoutsByPatient(patientId).enqueue(new Callback<List<WorkoutSession>>() {
            @Override
            public void onResponse(Call<List<WorkoutSession>> call, Response<List<WorkoutSession>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processWorkouts(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<WorkoutSession>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    private void processWorkouts(List<WorkoutSession> treinos) {
        runOnUiThread(() -> {
            tasksParaExibir.clear();

            // Converte a data selecionada no calendário para o dia da semana (ex: "SEG")
            String diaSemanaAbreviado = getDiaSemanaAbreviado(CalendarUtils.selectedDate.getDayOfWeek().getValue());

            for (WorkoutSession treino : treinos) {
                if (treino.getWeekDay() != null && treino.getWeekDay().trim().equalsIgnoreCase(diaSemanaAbreviado)) {
                    for (ExerciseSession session : treino.getExercises()) {
                        tasksParaExibir.add(new Task(
                                session.getExercise().getExercise_id(),
                                session.getExercise().getTitle(),
                                session.getSerie(),
                                session.getRepetitions(),
                                session.getExercise().getMidiaURL(),
                                session.getExercise().getDescription()
                        ));
                    }
                }
            }

            if (tasksParaExibir.isEmpty()) {
                tasksParaExibir.add(new Task(-1L, "Descanso! Sem exercícios.", 0, 0, "", ""));
            }
            taskAdapter.notifyDataSetChanged();
        });
    }

    private String getDiaSemanaAbreviado(int diaSemana) {
        String[] dias = {"", "SEG", "TER", "QUA", "QUI", "SEX", "SAB", "DOM"};
        return dias[diaSemana];
    }

   public void setupMenuClicks() {
       iconHome.setOnClickListener(v -> {
           startActivity(new Intent(this, MainActivity.class));
           overridePendingTransition(0, 0);
           finish();
       });

       // Clique na Agenda (Já está nela)
       iconCalendar.setOnClickListener(v -> {

       });

       // Clique no Perfil
       iconProfile.setOnClickListener(v -> {
           startActivity(new Intent(this, ProfileActivity.class));
           overridePendingTransition(0, 0);
           finish();
       });
    }

    // Métodos para os botões de setinha no XML
    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }
}