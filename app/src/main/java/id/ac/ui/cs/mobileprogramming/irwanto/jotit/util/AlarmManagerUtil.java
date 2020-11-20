package id.ac.ui.cs.mobileprogramming.irwanto.jotit.util;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.AlarmManagerCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Reminder;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.receiver.AlarmReceiver;

public class AlarmManagerUtil {
    public static void createAlarm(Context context, AlarmManager alarmManager, Reminder reminder) {
        PendingIntent pendingIntent = AlarmReceiver.getAlarmPendingIntent(context, reminder);

        AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.ELAPSED_REALTIME_WAKEUP, getTriggerTime(reminder), pendingIntent);
    }

    public static void cancelAlarm(Context context, AlarmManager alarmManager, Reminder reminder) {
        PendingIntent pendingIntent = AlarmReceiver.getAlarmPendingIntent(context, reminder);

        alarmManager.cancel(pendingIntent);
    }

    @SuppressLint("SimpleDateFormat")
    private static long getTriggerTime(Reminder reminder) {
        Date date = new Date();
        Date time = new Date();
        try {
            date = (new SimpleDateFormat("E, dd/MM/yyyy")).parse(reminder.date);
            time = (new SimpleDateFormat("HH:mm")).parse(reminder.time);
        } catch (ParseException e) {
            Log.e("AlarmManagerUtil", "Parsing failed");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        calendar.set(year, month, day, hour, minute);

        long timeDifference = calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        return SystemClock.elapsedRealtime() + timeDifference;
    }
}
