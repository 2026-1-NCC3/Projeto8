package com.example.projeto8.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.app.NotificationManager;
import com.example.projeto8.R;

public class AppointmentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String desc = intent.getStringExtra("description");
        String horario = intent.getStringExtra("horario");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "agendamento_channel")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Sessão amanhã com Maya Yamamoto")
                .setContentText("Horário: " + horario + " - " + desc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }
}
