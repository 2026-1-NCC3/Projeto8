package com.example.projeto8.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.app.NotificationManager;

public class AppointmentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String description = intent.getStringExtra("description");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "agendamento_channel")
                .setSmallIcon(com.example.projeto8.R.drawable.baseline_notifications_24)
                .setContentTitle("Sessão Maya Fisioterapia")
                .setContentText("Lembrete: " + description + " amanhã!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }
}