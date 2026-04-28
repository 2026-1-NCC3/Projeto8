package com.example.projeto8.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto8.R;
import com.example.projeto8.api.patient.PatientDTO.PatientResponseDTO;
import com.example.projeto8.api.patient.PatientService;
import com.example.projeto8.model.Patient;
import com.example.projeto8.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ImageView iconHome, iconExercise, iconProfile;

    private TextView txtName, txtStatus, txtEmail,txtPassword, txtCpf, txtCellphone, txtBirthDate,txtAge, txtGender, txtHeight, txtWeight, txtDescription, txtLGDP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        txtName = findViewById(R.id.txtName);
        txtStatus = findViewById(R.id.txtStatus);
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

        //  MENU
        iconHome = findViewById(R.id.iconHome);
        iconExercise = findViewById(R.id.iconExercise);
        iconProfile = findViewById(R.id.iconProfile);
        setupMenuClicks();

        //  PEGAR NOME DO LOGIN
        SharedPreferences prefs = getSharedPreferences("STORAGE", MODE_PRIVATE);
        String patient_id = prefs.getString("patient_id", null);

        if (patient_id != null) {
            loadPatient(patient_id);
        } else {
            Log.e("PROFILE", "Paciente não encontrado no SharedPreferences");
        }
    }

    private void loadPatient(String UUID) {
        PatientService service = RetrofitClient.getPatientService();

        service.getPatientById(UUID)
                .enqueue(new Callback<PatientResponseDTO>() {

                    @Override
                    public void onResponse(Call<PatientResponseDTO> call, Response<PatientResponseDTO> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            PatientResponseDTO patient = response.body();

                            txtName.setText(patient.getName() + " " + patient.getSurname());
                            txtStatus.setText("Status: " + patient.getStatus());

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

                            if(patient.getDescription() != null) {
                                txtDescription.setText(patient.getDescription());
                            }
                        } else {
                            Log.e("API_ERROR", "Resposta vazia");
                        }
                    }

                    @Override
                    public void onFailure(Call<PatientResponseDTO> call, Throwable t) {
                        Log.e("API_ERROR", "Falha na requisição: " + t.getMessage());
                    }
                });
    }

    // MENU
    private void setupMenuClicks() {
        iconHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        iconExercise.setOnClickListener(v -> {
            startActivity(new Intent(this, ExercisesActivity.class));
            finish();
        });

        iconProfile.setOnClickListener(v -> {
            // já está aqui
        });
    }
}