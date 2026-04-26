package com.example.projeto8.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.example.projeto8.UI.AppointmentReceiver;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class NotificationScheduler {

    public static void schedule(Context context, LocalDateTime appointmentDate, String description, String time) {
        //Converte a data para milissegundos
        ZonedDateTime zdt = appointmentDate.atZone(ZoneId.systemDefault());
        long appointmentMillis = zdt.toInstant().toEpochMilli();

        //Define o tempo: exatos 24 horas antes
        long notifyTime = appointmentMillis - (24 * 60 * 60 * 1000);

        // Se já passou das 24h anteriores (ex: consulta é daqui a 2h), não agenda
        if (System.currentTimeMillis() > notifyTime) return;

        Intent intent = new Intent(context, AppointmentReceiver.class);
        intent.putExtra("description", description);
        intent.putExtra("horario", time);

        // id baseado no horário do appt para não sobrescrever outros agendamentos
        int uniqueId = (int) (appointmentMillis / 1000);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                uniqueId,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notifyTime, pendingIntent);
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notifyTime, pendingIntent);
                }
            } else {
                // Para versões antigas do Android
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notifyTime, pendingIntent);
            }
        }
    }
}