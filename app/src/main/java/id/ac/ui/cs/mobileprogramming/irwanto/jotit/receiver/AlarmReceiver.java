package id.ac.ui.cs.mobileprogramming.irwanto.jotit.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Reminder;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast toast = Toast.makeText(context, "Alarm of " + intent.getStringExtra("reminder_title"), Toast.LENGTH_LONG);
        toast.show();
    }

    public static PendingIntent getAlarmPendingIntent(Context context, Reminder reminder) {
        Bundle extras = new Bundle();
        extras.putString("reminder_reminderId", reminder.reminderId);
        extras.putInt("reminder_id", reminder.id);
        extras.putString("reminder_title", reminder.title);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtras(extras);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }
}
