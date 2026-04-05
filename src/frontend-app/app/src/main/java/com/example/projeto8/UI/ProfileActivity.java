package com.example.projeto8.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto8.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable (this);
        setContentView(R.layout.menu);

        ImageView iconHome = findViewById(R.id.iconHome);
        ImageView iconExercise = findViewById(R.id.iconExercise);
        ImageView iconProfile = findViewById(R.id.iconProfile);


        iconHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        iconExercise.setOnClickListener(v -> {
            startActivity(new Intent(this, ExercisesActivity.class));
            finish();
        });

        iconProfile.setOnClickListener(v -> {
            // Não faz nada porque já estamos no profile
        });

    }
}

