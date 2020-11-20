package id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.database.AppDatabase;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.database.ReminderDAO;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Reminder;

public class ReminderRepository {
    private ReminderDAO reminderDAO;

    public ReminderRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        reminderDAO = db.reminderDAO();
    }

    public LiveData<List<Reminder>> getAllReminders() {
        return reminderDAO.getAllReminders();
    }

    public LiveData<Reminder> getLiveDataReminderById(String reminderId) {
        return reminderDAO.getLivaDataReminderById(reminderId);
    }

    public Reminder getLatestReminder() {
        Future<Reminder> future = AppDatabase.databaseExecutor.submit(new Callable<Reminder>() {
            @Override
            public Reminder call() throws Exception {
                return reminderDAO.getLatestReminder();
            }
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("ReminderRepository", e.getMessage());
        }
        return null;
    }

    public void insert(Reminder reminder) {
        AppDatabase.databaseExecutor.execute(() -> {
            reminderDAO.insertReminder(reminder);
        });
    }

    public void update(Reminder reminder) {
        AppDatabase.databaseExecutor.execute(() -> {
            reminderDAO.updateReminder(reminder);
        });
    }

    public void delete(Reminder reminder) {
        AppDatabase.databaseExecutor.execute(() -> {
            reminderDAO.deleteReminder(reminder);
        });
    }
}
