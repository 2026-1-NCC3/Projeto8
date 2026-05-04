package com.example.projeto8.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto8.R;

public class LoadScreen extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.load_screen);

            if (getSupportActionBar() != null) getSupportActionBar().hide();

            // tempo de execução da tela pra ir pra tela de login (tem que ser em milissegundos)
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(LoadScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }, 5000);
        }
    }

