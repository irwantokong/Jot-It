package id.ac.ui.cs.mobileprogramming.irwanto.jotit.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class RemainingTimeService extends Service {
    public static final String REMAINING_TIME_BR = "id.ac.ui.cs.mobileprogramming.irwanto.jotit.service.RemainingTimeService";
    Intent broadcastIntent = new Intent(REMAINING_TIME_BR);

    private long targetTime;
    private Handler remainingTimeHandler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        remainingTimeHandler.removeCallbacks(sendRemainingTime);
        remainingTimeHandler.postDelayed(sendRemainingTime, 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        targetTime = intent.getLongExtra("target_time", System.currentTimeMillis());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        remainingTimeHandler.removeCallbacks(sendRemainingTime);
    }

    private Runnable sendRemainingTime = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            broadcastIntent.putExtra("remaining_time", targetTime - currentTime);
            sendBroadcast(broadcastIntent);
            remainingTimeHandler.postDelayed(this, 1000);
        }
    };
}
