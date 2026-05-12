package com.example.projeto8.UI;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projeto8.UI.CustomTypefaceSpan;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;

import androidx.core.content.res.ResourcesCompat;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.projeto8.R;
import com.example.projeto8.api.patient.PatientDTO.PatientLoginRequestDTO;
import com.example.projeto8.api.patient.PatientDTO.PatientLoginResponseDTO;
import com.example.projeto8.api.patient.PatientService;
import com.example.projeto8.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    Button btn_accept_lgpd;
    EditText Email;
    EditText Password;
    TextView forgotPassword;

    TextView mensagePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        RetrofitClient.init(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.editEmail);
        Password = findViewById(R.id.editPassword);
        forgotPassword = findViewById(R.id.forgotPassword);
        mensagePassword = findViewById(R.id.mensagePassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            patientLogIn();
        });

        forgotPassword.setOnClickListener(v -> {
            forgotPatientPassword();
        });

        SpannableString hint = new SpannableString("Insira seu e-mail");

        Typeface typeface = ResourcesCompat.getFont(this, R.font.sourceitalic);

        hint.setSpan(new CustomTypefaceSpan(typeface), 0, hint.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Email.setHint(hint);

        SpannableString hintPass = new SpannableString("Insira sua senha");
        hintPass.setSpan(new CustomTypefaceSpan(typeface), 0, hintPass.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Password.setHint(hintPass);

    }

    public void forgotPatientPassword() {
        mensagePassword.setAlpha(0f);
        mensagePassword.setVisibility(View.VISIBLE);

        mensagePassword.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null);
    }

    public void patientLogIn() {
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        // Validar se o paciente preencheu os campos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        PatientLoginRequestDTO loginInfo = new PatientLoginRequestDTO(email, password);
        PatientService service = RetrofitClient.getPatientService();

        service.login(loginInfo).enqueue(new Callback<PatientLoginResponseDTO>() {
            @Override
            public void onResponse(Call<PatientLoginResponseDTO> call, Response<PatientLoginResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String tokenGerado = response.body().getToken();
                    String patientID = response.body().getId();
                    String patientName = response.body().getName();
                    String patientEmail = response.body().getEmail();
                    getSharedPreferences("STORAGE", MODE_PRIVATE)
                            .edit()
                            .putString("token", tokenGerado)
                            .putString("patient_id", patientID)
                            .putString("patient_name", patientName)
                            .putBoolean("lgpd_check", response.body().isLgpdCheck())
                            .apply();

                    Log.d("JSON_REAL", new com.google.gson.Gson().toJson(response.body()));

                    Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                    // Verificação para ver se o botão de lgpd já foi acessado pela conta
                    if (!response.body().isLgpdCheck()) {
                        LgpdPopUpScreen lgpdSheet = new LgpdPopUpScreen();
                        lgpdSheet.setCancelable(false);
                        lgpdSheet.show(getSupportFragmentManager(), "LGPD_CHECK");
                    } else {
                        // Após o login, manda os dados para a MainActivity (App Home)
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("PATIENT_ID", patientID);
                        intent.putExtra("AUTH_TOKEN", tokenGerado);
                        intent.putExtra("PATIENT_NAME", patientName);
                        intent.putExtra("PATIENT_EMAIL", patientEmail);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                    }
                } else {
                    if (response.code() == 401) {
                        Toast.makeText(LoginActivity.this, "E-mail ou senha incorretos.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Erro no servidor: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PatientLoginResponseDTO> call, Throwable t) {
                Log.e("API_ERROR", "Falha na conexão: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Não foi possível conectar ao servidor.", Toast.LENGTH_LONG).show();
            }
        });
    }

}
