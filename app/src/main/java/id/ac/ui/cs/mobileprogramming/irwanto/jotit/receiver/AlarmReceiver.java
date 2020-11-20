package id.ac.ui.cs.mobileprogramming.irwanto.jotit.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.R;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Reminder;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int reminderId = intent.getIntExtra("reminder_id", 0);
        String reminderTitle = intent.getStringExtra("reminder_title");
        String reminderDateAndTime = intent.getStringExtra("reminder_date") + " " + intent.getStringExtra("reminder_time");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id));
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(reminderTitle)
                .setContentText(reminderDateAndTime)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(reminderId, builder.build());
    }

    public static PendingIntent getAlarmPendingIntent(Context context, Reminder reminder) {
        Bundle extras = new Bundle();
        extras.putString("reminder_reminderId", reminder.reminderId);
        extras.putInt("reminder_id", reminder.id);
        extras.putString("reminder_title", reminder.title);
        extras.putString("reminder_date", reminder.date);
        extras.putString("reminder_time", reminder.time);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtras(extras);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }
}
