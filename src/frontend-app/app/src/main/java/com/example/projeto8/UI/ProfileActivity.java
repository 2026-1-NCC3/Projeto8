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
import com.example.projeto8.api.patient.PatientService;
import com.example.projeto8.model.Patient;
import com.example.projeto8.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ImageView iconHome, iconExercise, iconProfile;

    private TextView txtName, txtStatus, txtEmail, txtCpf, txtBirthDate,
            txtGender, txtHeight, txtWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.profile_activity);

        // ====== INICIALIZA VIEWS ======
        txtName = findViewById(R.id.txtName);
        txtStatus = findViewById(R.id.txtStatus);
        txtEmail = findViewById(R.id.txtEmail);
        txtCpf = findViewById(R.id.txtCpf);
        txtBirthDate = findViewById(R.id.txtBirthDate);
        txtGender = findViewById(R.id.txtGender);
        txtHeight = findViewById(R.id.txtHeight);
        txtWeight = findViewById(R.id.txtWeight);

        // ====== MENU ======
        iconHome = findViewById(R.id.iconHome);
        iconExercise = findViewById(R.id.iconExercise);
        iconProfile = findViewById(R.id.iconProfile);
        setupMenuClicks();

        // ====== PEGAR NOME DO LOGIN ======
        SharedPreferences prefs = getSharedPreferences("STORAGE", MODE_PRIVATE);
        String name = prefs.getString("patient_name", null);

        if (name != null) {
            loadPatient(name);
        } else {
            Log.e("PROFILE", "Nome não encontrado no SharedPreferences");
        }
    }

    // ====== BUSCAR PACIENTE PELO NOME ======
    private void loadPatient(String name) {
        PatientService service = RetrofitClient.getPatientService();

        service.getPatientByFullName(name, null) // surname = null
                .enqueue(new Callback<List<Patient>>() {

                    @Override
                    public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {

                            Patient p = response.body().get(0); // pega o primeiro

                            txtName.setText(p.getName() + " " + p.getSurname());
                            txtStatus.setText("Status: " + p.getStatus());

                            txtEmail.setText(p.getEmail());
                            txtCpf.setText(p.getCpf());
                            txtBirthDate.setText(p.getBirthDate());

                            txtGender.setText(p.getGender());
                            txtHeight.setText(p.getHeight());
                            txtWeight.setText(p.getWeight());

                        } else {
                            Log.e("API_ERROR", "Resposta vazia");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Patient>> call, Throwable t) {
                        Log.e("API_ERROR", "Erro: " + t.getMessage());
                    }
                });
    }

    // ====== MENU ======
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