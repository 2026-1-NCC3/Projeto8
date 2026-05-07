package com.example.projeto8.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.projeto8.R;
import com.example.projeto8.api.exerciseSession.ExerciseSessionDTO.ExerciseSessionRequestDTO;
import com.example.projeto8.api.exerciseSession.ExerciseSessionService;
import com.example.projeto8.api.workout.WorkoutService;
import com.example.projeto8.model.ExerciseSession;
import com.example.projeto8.model.Task;
import com.example.projeto8.model.WorkoutSession;
import com.example.projeto8.remote.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExercisesActivity extends AppCompatActivity {
    private ImageView imgExercise;
    private TextView textTitle, textDescription, txtSerieReps;
    private View btnPain;
    private ImageButton btnNext, btnBack;
    private ImageView iconHome, iconCalendar, iconProfile;

    private ArrayList<Task> listaExercicios;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        initWidgets();

        // Recupera a lista vinda da MainActivity
        listaExercicios = getIntent().getParcelableArrayListExtra("LISTA_EXERCICIOS");

        // Validação da lista
        if (listaExercicios != null && !listaExercicios.isEmpty()) {
            // Verifica se o primeiro item é o aviso de "Descanse"
            if (listaExercicios.get(0).getExerciseId() == -1L) {
                Toast.makeText(this, "Nada para treinar hoje!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            exibirExercicio(currentIndex);
        } else {
            Toast.makeText(this, "Erro ao carregar lista de exercícios.", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupListeners();
        setupMenu();
    }

    private void initWidgets() {
        imgExercise = findViewById(R.id.imgExercise);
        textTitle = findViewById(R.id.textExercise);
        textDescription = findViewById(R.id.txtDescription);
        txtSerieReps = findViewById(R.id.txtSerieReps);
        btnPain = findViewById(R.id.btnPain);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        // Widgets do Menu (incluído no layout via <include/>)
        iconHome = findViewById(R.id.iconHome);
        iconCalendar = findViewById(R.id.iconCalendar);
        iconProfile = findViewById(R.id.iconProfile);
    }

    private void exibirExercicio(int index) {
        Task task = listaExercicios.get(index);

        textTitle.setText(task.getTitle());
        textDescription.setText(task.getDescription());
        txtSerieReps.setText("Séries: " + task.getSerie() + ". Repetições: " + task.getReps());


        // Se for o primeiro exercício, esconde o botão de voltar
        if (index == 0) {
            btnBack.setVisibility(View.INVISIBLE);
        } else {
            btnBack.setVisibility(View.VISIBLE);
        }

        // Se for o último, o botão vira um check workout
        if (index == listaExercicios.size() - 1) {
            btnNext.setImageResource(R.drawable.ic_empty); // DEPOIS colocar um ícone de finalizar
        } else {
            btnNext.setImageResource(R.drawable.chevron); // Ícone normal de seta
        }

        // Reseta o botão de dor para a cor padrão (preto como no seu XML)
        btnPain.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));

        if (task.getMidiaURL() != null && !task.getMidiaURL().isEmpty()) {
            carregarMidia(task.getMidiaURL());
        } else {
            imgExercise.setImageResource(R.drawable.background2); // Placeholder
        }

        Log.d("SESSION", "Exibindo " + (index + 1) + " de " + listaExercicios.size());
    }

    private void finalizarTreinoNoBackend() {
        WorkoutService api = RetrofitClient.getWorkoutService();
        Long workoutId = getIntent().getLongExtra("WORKOUT_ID", -1L);

        if (workoutId != -1L) {
            api.checkWorkout(workoutId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ExercisesActivity.this, "Treino finalizado! Parabéns!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Log.e("API_ERRO", "Erro ao finalizar: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ExercisesActivity.this, "Erro ao salvar conclusão.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    private void setupListeners() {

        // Botão VOLTAR
        btnBack.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                exibirExercicio(currentIndex);
            }
        });

        // Botão PRÓXIMO
        btnNext.setOnClickListener(v -> {
            if (currentIndex < listaExercicios.size() - 1) {
                currentIndex++;
                exibirExercicio(currentIndex);
            } else {
                finalizarTreinoNoBackend();
            }
        });

        // Botão SENTI DOR

        btnPain.setOnClickListener(v -> {
            // Pega os dados do exercício atual na lista
            Task exercicioAtual = listaExercicios.get(currentIndex);

            // Pega o Patient ID do SharedPreferences
            SharedPreferences prefs = getSharedPreferences("STORAGE", MODE_PRIVATE);
            String patientId = prefs.getString("patient_id", null);

            //Pega o RequestDTO para montar o feelPain
            ExerciseSessionRequestDTO body = new ExerciseSessionRequestDTO(true);
            ExerciseSessionService api = RetrofitClient.getExerciseService();

            if (patientId != null && exercicioAtual.getExerciseId() != -1L) {
                api.updateExerciseSessionPain(patientId, exercicioAtual.getExerciseId(), body)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(ExercisesActivity.this, "Dor registrada!", Toast.LENGTH_SHORT).show();

                                    int corSucesso = Color.parseColor("#CCEE715F");
                                    btnPain.setBackgroundTintList(ColorStateList.valueOf(corSucesso));
                                } else {
                                    Log.e("API_PAIN", "Erro no servidor: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("API_PAIN", "Falha na rede: " + t.getMessage());
                            }
                        });
            }
        });

        // Clique na imagem para abrir o vídeo no YouTube
        imgExercise.setOnClickListener(v -> {
            String url = listaExercicios.get(currentIndex).getMidiaURL();
            if (url != null && !url.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }
    private void carregarMidia(String url) {
        if (url.contains("youtube.com") || url.contains("youtu.be")) {
            try {
                String videoId = "";
                if (url.contains("v=")) {
                    videoId = url.split("v=")[1];
                    int amp = videoId.indexOf('&');
                    if (amp != -1) videoId = videoId.substring(0, amp);
                } else if (url.contains("youtu.be/")) {
                    videoId = url.split("youtu.be/")[1];
                }

                String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/0.jpg";

                Glide.with(this)
                        .load(thumbnailUrl)
                        .placeholder(R.drawable.background2)
                        .into(imgExercise);

            } catch (Exception e) {
                Log.e("ERRO_GLIDE", "Erro ao processar URL do YouTube");
            }
        } else {
            // Caso seja uma URL de imagem direta
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.background2)
                    .into(imgExercise);
        }
    }

    private void setupMenu() {
        if (iconHome != null) {
            iconHome.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }

        if (iconCalendar != null) {
            iconCalendar.setOnClickListener(v -> {
                startActivity(new Intent(this, MonthCalendarActivity.class));
                finish();
            });
        }

        if (iconProfile != null) {
            iconProfile.setOnClickListener(v -> {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
            });
        }
    }
}