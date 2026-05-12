package com.example.projeto8.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto8.R;
import com.example.projeto8.api.patient.PatientService;
import com.example.projeto8.api.patient.PatientDTO.PatientResponseDTO;
import com.example.projeto8.remote.RetrofitClient;

import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtName, txtEmail, txtPassword, txtCpf,
            txtCellphone, txtBirthDate, txtAge, txtGender,
            txtHeight, txtWeight, txtDescription, txtLGDP, txtWorkoutCount;

    private ProgressBar pbWorkouts;
    private Button btnExcluirConta;

    // MENU
    private View containerCalendar, containerHome, containerProfile, btnCalendar, btnHome, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        initWidgets();
        setupMenuClicks();
        loadPatientFromPrefs();
    }

    private void initWidgets() {
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtCpf = findViewById(R.id.txtCpf);
        txtCellphone = findViewById(R.id.txtCellphone);
        txtBirthDate = findViewById(R.id.txtBirthDate);
        txtAge = findViewById(R.id.txtAge);
        txtGender = findViewById(R.id.txtGender);
        txtHeight = findViewById(R.id.txtHeight);
        txtWeight = findViewById(R.id.txtWeight);
        txtDescription = findViewById(R.id.txtDescription);
        txtLGDP = findViewById(R.id.txtLGDP);


        // PROGRESSO
        pbWorkouts = findViewById(R.id.pbWorkouts);
        txtWorkoutCount = findViewById(R.id.txtWorkoutCount);

        btnExcluirConta = findViewById(R.id.btnExcluirConta);
        if (btnExcluirConta != null) {
            btnExcluirConta.setOnClickListener(v -> showDeleteDialog());
        }


        View menuInclude = findViewById(R.id.menu);
        if (menuInclude != null) {
            btnHome = menuInclude.findViewById(R.id.btnHome);
            btnCalendar = menuInclude.findViewById(R.id.btnCalendar);
            btnProfile = menuInclude.findViewById(R.id.btnProfile);

            containerHome = menuInclude.findViewById(R.id.containerHomeSelect);
            containerCalendar = menuInclude.findViewById(R.id.containerCalendarSelect);
            containerProfile = menuInclude.findViewById(R.id.containerProfileSelect);

            if (btnProfile != null) {
                btnProfile.setSelected(true);
                if (containerProfile != null) {
                    containerProfile.setBackgroundResource(R.drawable.selected_item_bg);
                }
            }

            updateMenuSelection(
                    btnProfile,       // Selecionado
                    containerProfile, // Container Selecionado
                    btnHome, containerHome, btnCalendar, containerCalendar // TODOS os outros que devem ser resetados
            );
        }
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
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // BOTÃO CALENDÁRIO
        if (btnCalendar != null) {
            btnCalendar.setOnClickListener(v -> {
                animateClick(v);
                // Abre o Calendário e fecha a Profile
                Intent intent = new Intent(ProfileActivity.this, MonthCalendarActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // BOTÃO PERFIL (Onde você já está)
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
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


    private void loadPatientFromPrefs() {
        SharedPreferences prefs = getSharedPreferences("STORAGE", MODE_PRIVATE);
        String patientId = prefs.getString("patient_id", null);

        if (patientId != null) {
            loadPatient(patientId);
            loadWeeklyProgress(patientId);
        } else {
            Log.e("PROFILE", "Paciente não encontrado no SharedPreferences");
        }
    }

    private void loadPatient(String UUID) {
        PatientService service = RetrofitClient.getPatientService();

        service.getPatientById(UUID).enqueue(new Callback<PatientResponseDTO>() {
            @Override
            public void onResponse(Call<PatientResponseDTO> call, Response<PatientResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PatientResponseDTO patient = response.body();

                    txtName.setText(patient.getName() + " " + patient.getSurname());
                    txtEmail.setText(patient.getEmail());
                    txtPassword.setText(patient.getPassword());
                    txtCpf.setText(patient.getCpf());
                    txtBirthDate.setText(patient.getBirthDate());
                    txtGender.setText(patient.getGender());
                    txtHeight.setText(patient.getHeight() + " m");
                    txtWeight.setText(patient.getWeight() + " kg");
                    txtAge.setText(String.valueOf(patient.getPatientAge()));
                    txtCellphone.setText(patient.getCellPhone());

                    String lgpdStatus = patient.isLgpdCheck() ? "Aceito" : "Não Aceito";
                    txtLGDP.setText("Termos LGPD: " + lgpdStatus);

                    if (patient.getDescription() != null) {
                        txtDescription.setText(patient.getDescription());
                    }
                }
            }

            @Override
            public void onFailure(Call<PatientResponseDTO> call, Throwable t) {
                Log.e("API_ERROR", "Falha na requisição: " + t.getMessage());
            }
        });
    }

    private void loadWeeklyProgress(String patientId) {
        RetrofitClient.getWorkoutService().getWeeklyProgress(patientId)
                .enqueue(new Callback<Map<String, Integer>>() {
                    @Override
                    public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Integer total = response.body().get("total");
                            Integer completed = response.body().get("completed");

                            if (total != null && completed != null) {
                                pbWorkouts.setMax(total);
                                pbWorkouts.setProgress(completed);
                                txtWorkoutCount.setText(completed + " de " + total + " treinos concluídos");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                        Log.e("PROFILE", "Erro ao carregar progresso: " + t.getMessage());
                    }
                });
    }




    private void showDeleteDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_delete, null);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(view);
        android.app.AlertDialog dialog = builder.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            excluirConta();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void excluirConta() {
        String uuidStr = getSharedPreferences("STORAGE", MODE_PRIVATE).getString("patient_id", null);
        if (uuidStr == null) return;

        UUID patient_id = UUID.fromString(uuidStr);
        RetrofitClient.getPatientService().deletePatient(patient_id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Conta excluída com sucesso", Toast.LENGTH_SHORT).show();
                    getSharedPreferences("STORAGE", MODE_PRIVATE).edit().clear().apply();

                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();
                } else {
                    Log.e("DELETE_ERROR", "Erro ao deletar: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("DELETE_ERROR", "Falha na conexão: " + t.getMessage());
            }
        });
    }
}
