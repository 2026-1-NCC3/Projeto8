package com.example.projeto8;

import android.os.Bundle;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity);

        TextView forgotPassword = findViewById(R.id.forgotPassword);
        TextView mensagePassword = findViewById(R.id.mensagePassword);


        forgotPassword.setOnClickListener(v -> {
            mensagePassword.setVisibility(View.VISIBLE);
        });
    }
}
