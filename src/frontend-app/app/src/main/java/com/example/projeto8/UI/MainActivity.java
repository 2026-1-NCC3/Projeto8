package com.example.projeto8.UI;

import static com.example.projeto8.UI.CalendarUtils.daysInWeekArray;
import static com.example.projeto8.UI.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private ImageView iconHome, iconExercise, iconProfile; // menu


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); // carrega o XML principal

        // Inicializa todos os componentes do XML
        initWidgets();

        // ConfigurA o RecyclerView de Tarefas com uma lista vazia inicial
        tasksParaExibir = new ArrayList<>();
        adapter = new TaskAdapter(tasksParaExibir);
        recyclerTasks.setAdapter(adapter);


        // Configurações de Menu e Calendário
        setupMenuClicks();
        CalendarUtils.selectedDate = LocalDate.now();
        setWeekView();

        // Busca os dados na API

        //Mecânismo de buscar os dados que vieram da intent de login
        String idRecebido = getIntent().getStringExtra("PATIENT_ID");
        String nomeRecebido = getIntent().getStringExtra("PATIENT_NAME");

        //Assim que recebe o nome e id do paciente, muda o txtName na tela e faz a lógica de WorkoutSeshData
        if (nomeRecebido != null) {
            txtName.setText(nomeRecebido);
        }
        if (idRecebido != null) {
            WorkoutSeshData(idRecebido);
        }
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);

        txtName = findViewById(R.id.txtName);
        recyclerTasks = findViewById(R.id.recyclerTasks);
        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));

        iconHome = findViewById(R.id.iconHome);
        iconExercise = findViewById(R.id.iconExercise);
        iconProfile = findViewById(R.id.iconProfile);
    }
    private void setupMenuClicks() {
        iconHome.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        iconExercise.setOnClickListener(v -> startActivity(new Intent(this, ExercisesActivity.class)));
        iconProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }

    // Monta o calendário semanal
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

                            // 1. Primeiro loop: percorre cada Sessão de Treino (ex: Treino de Segunda)
                            for (WorkoutSession treino : listaDeTreinos) {

                                // 2. Segundo loop: percorre os exercícios dentro desse treino
                                if (treino.getExercises() != null) {
                                    for (ExerciseSession session : treino.getExercises()) {

                                        // Pegamos os dados do objeto Exercise que está dentro da Session
                                        // Se o seu model for Record no Java, use session.getExercise().title()
                                        // Se for classe comum, use session.getExercise().getTitle()
                                        String titulo = (session.getExercise() != null) ?
                                                session.getExercise().getTitle() : "Exercício s/ nome";

                                        String reps = session.getSerie() + "x" + session.getRepetitions();

                                        tasksParaExibir.add(new Task(titulo + " | " + reps));
                                    }
                                }
                            }

                            if (tasksParaExibir.isEmpty()) {
                                tasksParaExibir.add(new Task("Nenhum exercício para hoje"));
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
}
