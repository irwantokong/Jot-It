package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Reminder;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.ReminderRepository;

public class RemindersListViewModel extends AndroidViewModel {
    ReminderRepository reminderRepository;
    LiveData<List<Reminder>> allReminder;

    public RemindersListViewModel(@NonNull Application application) {
        super(application);
        reminderRepository = new ReminderRepository(application);
        allReminder = reminderRepository.getAllReminders();
    }

    public LiveData<List<Reminder>> getAllReminder() {
        return allReminder;
    }
}