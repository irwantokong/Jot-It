package id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

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

    public Reminder getReminderById(String reminderId) {
        return reminderDAO.getReminderById(reminderId);
    }

    void insert(Reminder reminder) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reminderDAO.insertReminder(reminder);
        });
    }

    void update(Reminder reminder) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reminderDAO.updateReminder(reminder);
        });
    }

    void delete(Reminder reminder) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reminderDAO.deleteReminder(reminder);
        });
    }
}
