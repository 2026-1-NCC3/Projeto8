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
            carregarMidia(currentVideoUrl);
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

    // Método que recorta a URL do youtube para pegar a thumb e mantem a imagem como um botão do link do youutube
    private void carregarMidia(String url) {

        if (url.contains("youtube.com") || url.contains("youtu.be")) {
            try {
                String videoId;
                // Pega o que vem depois do "v=" na URL do Youtube
                if (url.contains("v=")) {
                    videoId = url.split("v=")[1];
                    // Remove parâmetros extras que o youtube às vezes coloca (ex: &t=10s)
                    int amp = videoId.indexOf('&');
                    if (amp != -1) {
                        videoId = videoId.substring(0, amp);
                    }
                } else if (url.contains("youtu.be/")) {
                    videoId = url.split("youtu.be/")[1];
                } else {
                    videoId = "";
                }
                // Monta o link da imagem oficial do YouTube
                String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/0.jpg";

                // Carrega a imagem com o Glide
                Glide.with(this)
                        .load(thumbnailUrl)
                        .into(imgExercise);

            } catch (Exception e) {
                Log.e("ERRO", "Erro ao carregar thumbnail");
            }

        } else {
            // É imagem normal
            Glide.with(this)
                    .load(url)
                    .into(imgExercise);
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