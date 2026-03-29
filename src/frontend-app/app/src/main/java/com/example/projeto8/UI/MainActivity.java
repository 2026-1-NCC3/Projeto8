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

        // 1. PRIMEIRO: Inicialize todos os componentes do XML
        initWidgets();

        // 2. Configure o RecyclerView de Tarefas com uma lista vazia inicial
        tasksParaExibir = new ArrayList<>();
        adapter = new TaskAdapter(tasksParaExibir);
        recyclerTasks.setAdapter(adapter);


        // 3. Configurações de Menu e Calendário
        setupMenuClicks();
        CalendarUtils.selectedDate = LocalDate.now();
        setWeekView();

        // 4. POR ÚLTIMO: Busca os dados na API
        //String meuIdReal = "3e8e4187-47d8-4751-955d-e6a036db9478";

        //Mecânismo de buscar os dados que vieram da intent de login
        String idRecebido = getIntent().getStringExtra("PATIENT_ID");
        carregarDadosDoPaciente(UUID.fromString(idRecebido));
        

    }
     // código fica mais limpo, initWidgets() = preparar os atores
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
    private void carregarDadosDoPaciente(UUID patientId) {
        // Usa o RetrofitClient que você criou para fazer o pedido
        WorkoutService api = RetrofitClient.getWorkoutService();

        api.getWorkoutsByPatient(patientId).enqueue(new Callback<List<WorkoutSession>>() {
            @Override
            public void onResponse(Call<List<WorkoutSession>> call, Response<List<WorkoutSession>> response) {
                // 1. Log de confirmação (que já vimos que funciona)
                android.util.Log.d("TESTE_API", "O servidor respondeu agora!");

                // 2. TUDO que mexe na tela precisa de permissão do Android para rodar na UI Thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            //MUDAR AQUI OS DADOS MOCKADOS PARA OS REAIS VINDOS DO LOGIN
                            // Mudar o texto do nome
                            txtName.setText("Jorge");
                            android.util.Log.d("TESTE_API", "Texto do nome alterado!");

                            // Criar os dados de teste
                            ArrayList<Task> listaTeste = new ArrayList<>();
                            listaTeste.add(new Task("Teste: Agachamento"));
                            listaTeste.add(new Task("Teste: Flexão"));

                            // Atualizar a lista e avisar o adapter
                            tasksParaExibir.clear();
                            tasksParaExibir.addAll(listaTeste);
                            adapter.notifyDataSetChanged();

                            android.util.Log.d("TESTE_API", "Lista de tarefas atualizada no Adapter!");

                        } catch (Exception e) {
                            android.util.Log.e("TESTE_API", "Erro ao atualizar interface: " + e.getMessage());
                        }
                    }
                });
            }


            @Override
            public void onFailure(Call<List<WorkoutSession>> call, Throwable t) {
                Log.e("API_ERRO", "Mensagem: " + t.getMessage());
                txtName.setText("ERRO DE CONEXÃO: " + t.getMessage());
            }
        });
    }

    }
