package com.example.projeto8.UI;

import static com.example.projeto8.UI.CalendarUtils.daysInWeekArray;
import static com.example.projeto8.UI.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.projeto8.R;
import com.example.projeto8.adapter.TaskAdapter;
import com.example.projeto8.model.Exercise;
import com.example.projeto8.model.ExerciseSession;
import com.example.projeto8.model.Task;
import com.example.projeto8.model.WorkoutSession;
import com.example.projeto8.api.workout.WorkoutService;
import com.example.projeto8.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText; // texto "Feb 2026"
    private RecyclerView calendarRecyclerView; // calendário (dias)
    private TextView txtName;
    private RecyclerView recyclerTasks;
    private TaskAdapter adapter;
    private ArrayList<Task> tasksParaExibir;
    ImageView iconHome, iconCalendar, iconProfile; // menu
    View bntHome,bntCalendar,bntProfile;
    private Button btnStartWorkout;

    @Override
    protected void onResume() {
        super.onResume();

        // SharedPreferences prefs = getSharedPreferences("STORAGE", MODE_PRIVATE);
        /*
        String idRecebido = prefs.getString("patient_id", null);
        String nomeRecebido = prefs.getString("patient_name", null);

        if (nomeRecebido != null) {
            txtName.setText(nomeRecebido);
        }

        if (idRecebido != null) {
            WorkoutSeshData(idRecebido);
            checkAppointmentsData(idRecebido); //Busca os agendamentos do paciente
        }
        */
        carregarDadosMockados();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); // carrega o XML principal

        // Inicializa todos os componentes do XML
        initWidgets();

        // ConfigurA o RecyclerView de Tarefas com uma lista vazia inicial
        tasksParaExibir = new ArrayList<>();
        adapter = new TaskAdapter(tasksParaExibir, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                Intent intent = new Intent(MainActivity.this, ExercisesActivity.class);

                // Passamos os dados que já temos direto pra próxima tela!
                intent.putExtra("EXERCISE_TITLE", task.getTitle());
                intent.putExtra("EXERCISE_MEDIA_URL", task.getMidiaURL());
                intent.putExtra("EXERCISE_DESC", task.getDescription());

                startActivity(intent);
            }
        });
        recyclerTasks.setAdapter(adapter);

        // Configurações de Menu e Calendário
        setupMenuClicks();
        CalendarUtils.selectedDate = LocalDate.now();
        setWeekView();

        // Busca os dados na API

        //Mecânismo de buscar os dados que vieram da intent de login, nao funciona se nao vier da login, testando o onResume()
        //String idRecebido = getIntent().getStringExtra("PATIENT_ID");
        //String nomeRecebido = getIntent().getStringExtra("PATIENT_NAME");

    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);

        txtName = findViewById(R.id.txtName);
        recyclerTasks = findViewById(R.id.recyclerTasks);
        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));

        View menuView = findViewById(R.id.menu);
        iconCalendar = findViewById(R.id.iconCalendar);
        iconHome = findViewById(R.id.iconHome);
        iconProfile = findViewById(R.id.iconProfile);

        bntHome = menuView.findViewById(R.id.btnHome);
        bntCalendar = menuView.findViewById(R.id.btnCalendar);
        bntProfile = menuView.findViewById(R.id.btnProfile);

        if (bntHome != null) bntHome.setSelected(true);
        btnStartWorkout = findViewById(R.id.btnStartWorkout);
    }

    private void setupMenuClicks() {

        // Pegamos os layouts dos botões
        View btnCalendar = findViewById(R.id.btnCalendar);
        View btnHome = findViewById(R.id.btnHome);
        View btnProfile = findViewById(R.id.btnProfile);

        // Configura o clique da Agenda
        btnCalendar.setOnClickListener(v -> {
            startActivity(new Intent(this, MonthCalendarActivity.class));
            overridePendingTransition(0, 0);
        });

        // Clique na Home (já está nela, não precisa fazer nada ou apenas scroll up)
        btnHome.setOnClickListener(v -> {
            recyclerTasks.smoothScrollToPosition(0);
        });

        // Clique no Perfil
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            overridePendingTransition(0, 0);
        });

        // Botão de Iniciar Treino (Lógica separada do menu)
        btnStartWorkout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExercisesActivity.class);
            intent.putParcelableArrayListExtra("LISTA_EXERCICIOS", tasksParaExibir);
            startActivity(intent);
        });
    }

    // Monta o calendário semanal
    private void setWeekView() {

        // coloca "Mar 2026"
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));

        // pega os 7 dias da semana
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        // cria o adapter (responsável por desenhar cada dia)
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this, R.drawable.selected_day_bg);
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

            SharedPreferences prefs = getSharedPreferences("STORAGE", MODE_PRIVATE);
            String id = prefs.getString("patient_id", null);

            if (id != null) {
                WorkoutSeshData(id);
            }
        }
    }

    //Para associar o dia do exercicio com o dia da semana
    private String getDiaSemanaAbreviado(int diaSemana) {
        switch (diaSemana) {
            case 1: return "SEG";
            case 2: return "TER";
            case 3: return "QUA";
            case 4: return "QUI";
            case 5: return "SEX";
            case 6: return "SAB";
            case 7: return "DOM";
            default: return "";
        }
    }
    private void carregarDadosMockados() {
        tasksParaExibir.clear();

        // Adicionando exercícios de teste
        tasksParaExibir.add(new Task(1L, "Agachamento Livre", 3, 15, "https://www.youtube.com/watch?v=aclHkVaku9U", "Mantenha as costas retas e desça até 90 graus."));
        tasksParaExibir.add(new Task(2L, "Flexão de Braços", 4, 10, "https://www.youtube.com/watch?v=vp7pYV4X_O4", "Mantenha o corpo alinhado, sem descer o quadril."));
        tasksParaExibir.add(new Task(3L, "Prancha Abdominal", 3, 30, "https://www.youtube.com/watch?v=pvIjsGZ0Zbc", "Segure por 30 segundos mantendo o core contraído."));

        // Atualiza a interface
        adapter.notifyDataSetChanged();

        // Faz o botão aparecer, já que agora temos dados
        if (btnStartWorkout != null) {
            btnStartWorkout.setVisibility(View.VISIBLE);
        }
    }
    //Mostrar a Workout do dia
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

                                int diaSemana = CalendarUtils.selectedDate.getDayOfWeek().getValue();
                                String diaAtual = getDiaSemanaAbreviado(diaSemana);

                                for (WorkoutSession treino : listaDeTreinos) {

                                    if (treino.getWeekDay() != null &&
                                            treino.getWeekDay().trim().toUpperCase().equals(diaAtual)) {

                                        if (treino.getExercises() != null) {
                                            for (ExerciseSession session : treino.getExercises()) {

                                                int serie = session.getSerie();
                                                int reps = session.getRepetitions();

                                                String titulo = "Exercício s/ nome";
                                                Long exercise_id = -1L;
                                                String midiaURL = "";
                                                String description = "";

                                                if (session.getExercise() != null) {
                                                    if (session.getExercise().getTitle() != null)
                                                        titulo = session.getExercise().getTitle();
                                                    if (session.getExercise().getExercise_id() != null)
                                                        exercise_id = session.getExercise().getExercise_id();
                                                    if (session.getExercise().getMidiaURL() != null)
                                                        midiaURL = session.getExercise().getMidiaURL();
                                                    if (session.getExercise().getDescription() != null)
                                                        description = session.getExercise().getDescription();
                                                }

                                                tasksParaExibir.add(new Task(exercise_id, titulo, serie, reps, midiaURL, description));
                                            }
                                        }
                                    }
                                }

                                if (tasksParaExibir.isEmpty()) {
                                    tasksParaExibir.add(
                                            new Task(-1L, "Nenhum exercício para hoje! Descanse.", 0, 0, "", "")
                                    );
                                }
                                adapter.notifyDataSetChanged();
                                Log.d("TESTE_API", "Exercícios carregados: " + tasksParaExibir.size());

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

    //Buscar os appointments depois de retornar os dados do paciente no Login, juntos dos exercicios
    private void checkAppointmentsData(String patientId) {
        AppointmentService api = RetrofitClient.getAppointmentService();
        api.getAppointmentsByPatient(patientId).enqueue(new Callback<List<AppointmentResponseDTO>>() {
            @Override
            public void onResponse(Call<List<AppointmentResponseDTO>> call, Response<List<AppointmentResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AppointmentResponseDTO> agendamentos = response.body();

                    for (AppointmentResponseDTO appo : response.body()) {
                        try {
                            String fullDateFromApi = appo.getDate(); // No banco vem "2026-04-27T00:00:00"
                            String timeFromApi = appo.getTime();     // Vem "10:30"

                            // Pegamos os primeiros 10 digitos da data (YYYY-MM-DD) contando os -
                            String cleanDate = fullDateFromApi.substring(0, 10);

                            //Monta a String no formato do LocalDateTime
                            String isoDateTime = cleanDate + "T" + timeFromApi;

                            // Se o time vier "10:30", isso ajusta os segundos
                            if (timeFromApi.length() == 5) {
                                isoDateTime += ":00";
                            }
                            LocalDateTime dataEHoraReal = LocalDateTime.parse(isoDateTime);
                            NotificationScheduler.schedule(
                                    MainActivity.this,
                                    dataEHoraReal,
                                    appo.getDescription(),
                                    appo.getTime());

                            Log.d("NOTIF_SUCESSO", "Agendado para: " + isoDateTime + " - " + appo.getDescription());
                        } catch (Exception e) {
                            Log.e("NOTIF_ERRO", "Falha ao processar data/hora: " + appo.getDate() + " " + appo.getTime());
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<AppointmentResponseDTO>> call, Throwable t) {
                Log.e("API_APPOINTMENT", "Erro ao buscar agendamentos: " + t.getMessage());
            }
        });
    }







}
