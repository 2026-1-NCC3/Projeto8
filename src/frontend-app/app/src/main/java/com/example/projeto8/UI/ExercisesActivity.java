package com.example.projeto8.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageView;
import android.net.Uri;
import com.bumptech.glide.Glide;
import com.example.projeto8.R;
import com.example.projeto8.UI.ExerciseURL;

public class ExercisesActivity extends AppCompatActivity {
    ImageView iconHome;
    ImageView iconExercise;
    ImageView iconProfile;
    ImageView imgExercise;
    String videoUrl = "https://www.youtube.com/watch?v=aclHkVaku9U";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_activity);

        imgExercise = findViewById(R.id.imgExercise);
        ImageView iconHome = findViewById(R.id.iconHome);
        ImageView iconExercise = findViewById(R.id.iconExercise);
        ImageView iconProfile = findViewById(R.id.iconProfile);

        String videoId = "aclHkVaku9U";
        String thumbnailUrl =
                "https://img.youtube.com/vi/" + videoId + "/0.jpg";

        Glide.with(this)
                .load(thumbnailUrl)
                .into(imgExercise);

        ExerciseURL legpress = new ExerciseURL(
                "Leg Press",
                "https://www.youtube.com/watch?v=IZxyjW7MPJQ"

        );
        imgExercise.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            startActivity(intent);

        });

        iconHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        iconExercise.setOnClickListener(v -> {
            startActivity(new Intent(this, ExercisesActivity.class));
        });

        iconProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });
    }

}

