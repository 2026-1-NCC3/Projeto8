package com.example.projeto8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity);

        TextView forgotPassword = findViewById(R.id.forgotPassword);
        TextView mensagePassword = findViewById(R.id.mensagePassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {

            Intent intent = new Intent(LoginActivity.this, ExercisesActivity.class);
            startActivity(intent);

        });


        forgotPassword.setOnClickListener(v -> {
            mensagePassword.setVisibility(View.VISIBLE);
        });
    }
}
