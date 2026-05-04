package com.example.projeto8.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    // TEXTOS
    private TextView txtName, txtStatus, txtEmail, txtPassword, txtCpf,
            txtCellphone, txtBirthDate, txtAge, txtGender,
            txtHeight, txtWeight, txtDescription, txtLGDP;

    private LinearLayout btnExcluir;

    // MENU
    private View btnCalendar, btnHome, btnProfile;
    private View containerCalendar, containerHome, containerProfile;

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

        // TEXTOS
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


        // MENU
        View menuInclude = findViewById(R.id.menu);
        if (menuInclude != null) {
            btnHome = menuInclude.findViewById(R.id.btnHome);
            btnCalendar = menuInclude.findViewById(R.id.btnCalendar);
            btnProfile = menuInclude.findViewById(R.id.btnProfile);

            containerHome = menuInclude.findViewById(R.id.containerHomeSelect);
            containerCalendar = menuInclude.findViewById(R.id.containerCalendarSelect);
            containerProfile = menuInclude.findViewById(R.id.containerProfileSelect);

            updateMenuSelection(
                    btnProfile, containerProfile,
                    btnHome, containerHome,
                    btnCalendar, containerCalendar
            );
        }

    }

    private void loadPatientFromPrefs() {
        SharedPreferences prefs = getSharedPreferences("STORAGE", MODE_PRIVATE);
        String patientId = prefs.getString("patient_id", null);

        if (patientId != null) {
            loadPatient(patientId);
        } else {
            Log.e("PROFILE", "Paciente não encontrado no SharedPreferences");
        }
    }

    public void setupMenuClicks() {

        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                updateMenuSelection(
                        btnProfile, containerProfile,
                        btnHome, containerHome,
                        btnCalendar, containerCalendar
                );
            });
        }

        if (btnHome != null) {
            btnHome.setOnClickListener(v -> {
                updateMenuSelection(
                        btnHome, containerHome,
                        btnProfile, containerProfile,
                        btnCalendar, containerCalendar
                );
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
            });
        }

        if (btnCalendar != null) {
            btnCalendar.setOnClickListener(v -> {
                updateMenuSelection(
                        btnCalendar, containerCalendar,
                        btnHome, containerHome,
                        btnProfile, containerProfile
                );
                startActivity(new Intent(this, MonthCalendarActivity.class));
                overridePendingTransition(0, 0);
                finish();
            });
        }
    }

    private void updateMenuSelection(View selectedBtn, View selectedContainer, View... others) {
        for (View view : others) {
            if (view != null) {
                view.setSelected(false);

                if (view == containerHome || view == containerCalendar || view == containerProfile) {
                    view.setBackground(null);
                }
            }
        }

        if (selectedBtn != null) selectedBtn.setSelected(true);

        if (selectedContainer != null) {
            selectedContainer.setBackgroundResource(R.drawable.selected_item_bg);
        }
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
            // TODO: implementar exclusão
            dialog.dismiss();
        });

        dialog.show();
        view.findViewById(R.id.cardContent).setClipToOutline(true);
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
}