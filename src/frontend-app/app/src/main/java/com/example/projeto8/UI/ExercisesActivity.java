package com.example.projeto8.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projeto8.R;

public class ExercisesActivity extends AppCompatActivity {
    private ImageView iconHome, iconExercise, iconProfile, imgExercise;
    private TextView textTitle, textDescription;
    private String currentVideoUrl = ""; // Guarda a URL para quando o paciente clicar na imagem

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_activity);

        imgExercise = findViewById(R.id.imgExercise);
        textTitle = findViewById(R.id.textExercise); // Aquele seu TextView de 33dp

        textDescription = findViewById(R.id.txtDescription);

        iconHome = findViewById(R.id.iconHome);
        iconExercise = findViewById(R.id.iconExercise);
        iconProfile = findViewById(R.id.iconProfile);

        String title = getIntent().getStringExtra("EXERCISE_TITLE");
        currentVideoUrl = getIntent().getStringExtra("EXERCISE_MEDIA_URL");
        String desc = getIntent().getStringExtra("EXERCISE_DESC");

        if (title != null && !title.isEmpty()) {
            textTitle.setText(title);
        }
        if (desc != null && !desc.isEmpty()) {
            textDescription.setText(desc);
        }

        if (currentVideoUrl != null && !currentVideoUrl.isEmpty()) {
            carregarThumbnail(currentVideoUrl);
        } else {
            // Se o backend não enviou URL de vídeo
            Toast.makeText(this, "Nenhum vídeo disponível para este exercício.", Toast.LENGTH_SHORT).show();
        }

        // 5. CLIQUE NA IMAGEM PARA ABRIR O VÍDEO NO YOUTUBE
        imgExercise.setOnClickListener(v -> {
            if (currentVideoUrl != null && !currentVideoUrl.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentVideoUrl));
                startActivity(intent);
            }
        });

        // Configura o menu inferior
        setupMenu();
    }

    //TESTAR ISSO!! NAO CONSIDERAR AINDA
    // Método que recorta a URL do youtube para pegar só o ID da imagem
    private void carregarThumbnail(String urlDoYoutube) {
        if (urlDoYoutube.contains("v=")) {
            try {
                // Pega o que vem depois do "v=" na URL
                String[] splitUrl = urlDoYoutube.split("v=");
                String videoId = splitUrl[1];

                // Remove parâmetros extras que o youtube às vezes coloca (ex: &t=10s)
                int ampersandPosition = videoId.indexOf('&');
                if (ampersandPosition != -1) {
                    videoId = videoId.substring(0, ampersandPosition);
                }

                // Monta o link da imagem oficial do YouTube
                String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/0.jpg";

                // Carrega a imagem com o Glide
                Glide.with(this)
                        .load(thumbnailUrl)
                        .into(imgExercise);

            } catch (Exception e) {
                Log.e("YOUTUBE_ERRO", "Erro ao extrair ID do vídeo: " + e.getMessage());
            }
        }
    }

    private void setupMenu() {
        iconHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        iconExercise.setOnClickListener(v -> {
            // Já estamos na tela de exercícios
        });
        iconProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });
    }
}