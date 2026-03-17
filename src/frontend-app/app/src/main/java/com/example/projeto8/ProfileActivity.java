package com.example.projeto8;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private ImageView btnBack;
    private TextView tvNome;
    private EditText inputCPF;
    private EditText inputEmail;
    private EditText inputNumero;
    private Spinner spinnerGenero;
    private EditText inputAltura;
    private EditText inputPeso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        initializeViews();
        setupListeners();
        loadMockUserData();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        tvNome = findViewById(R.id.nome);
        inputCPF = findViewById(R.id.inputCPF);
        inputEmail = findViewById(R.id.inputEmail);
        inputNumero = findViewById(R.id.inputNumero);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        inputAltura = findViewById(R.id.inputAltura);
        inputPeso = findViewById(R.id.inputPeso);
    }

    private void setupListeners() {
        // Closes the current activity and returns to the previous one
        btnBack.setOnClickListener(v -> finish());
    }

    // Placeholder!
    private void loadMockUserData() {
        tvNome.setText("Hugo Palmeiras");
        inputCPF.setText("123.456.789-00");
        inputEmail.setText("HugoPalmeiras@gmail.com");
        inputNumero.setText("11 982339559");
        inputAltura.setText("1.80");
        inputPeso.setText("75.5");
    }

    // Placeholder!
    private void collectDataForBackend() {
        String nome = tvNome.getText().toString().trim();
        String cpf = inputCPF.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String telefone = inputNumero.getText().toString().trim();
        String genero = spinnerGenero.getSelectedItem().toString();
        String altura = inputAltura.getText().toString().trim();
        String peso = inputPeso.getText().toString().trim();

        // TODO: Validate inputs, then serialize to JSON and send to backend

        Toast.makeText(this, "Dados prontos para envio!", Toast.LENGTH_SHORT).show();
    }
}
