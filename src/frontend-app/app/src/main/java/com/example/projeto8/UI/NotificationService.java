package com.example.projeto8.UI;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.projeto8.R;

public class NotificationService extends AppCompatActivity {
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o){
                Toast.makeText(NotificationService.this, "Permitir postar notificações", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(NotificationService.this, "Permissão para postar notificações negada", Toast.LENGTH_SHORT).show();
            }
        }
    });

    private static final String CHANNEL_ID = "agendamento_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.notification_activity);

        createNotificationChannel();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notificações de Agendamento",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Canal para avisos de sessões de fisioterapia");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
