package com.example.projeto8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageView;
import android.net.Uri;
import com.bumptech.glide.Glide;

public class ExercisesActivity extends AppCompatActivity {
    ImageView imgExercise;
    String videoUrl = "https://www.youtube.com/watch?v=aclHkVaku9U";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_activity);

        imgExercise = findViewById(R.id.imgExercise);

        String videoId = "aclHkVaku9U";
        String thumbnailUrl =
                "https://img.youtube.com/vi/" + videoId + "/0.jpg";

        Glide.with(this)
                .load(thumbnailUrl)
                .into(imgExercise);

        Exercise legpress = new Exercise(
                "Leg Press",
                "https://www.youtube.com/watch?v=IZxyjW7MPJQ"

        );
        imgExercise.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            startActivity(intent);

        });
    }

}

