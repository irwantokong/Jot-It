package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Reminder;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.ReminderRepository;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.util.AlarmManagerUtil;

public class EditReminderViewModel extends AndroidViewModel {
    private final ReminderRepository reminderRepository;
    private final AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
    private Reminder editableReminder;

    public LiveData<Reminder> loadedReminder;
    public MutableLiveData<String> titleField = new MutableLiveData<>();
    public MutableLiveData<String> dateField = new MutableLiveData<>();
    public MutableLiveData<String> timeField = new MutableLiveData<>();

    public EditReminderViewModel(Application application) {
        super(application);
        reminderRepository = new ReminderRepository(application);
    }

    @SuppressLint("SimpleDateFormat")
    public void initReminder(String reminderId, boolean isEdit) {
        if (isEdit) {
            loadedReminder = reminderRepository.getLiveDataReminderById(reminderId);
        } else {
            editableReminder = new Reminder();
            editableReminder.date = new SimpleDateFormat("E, dd/MM/yyyy").format(new Date());
            editableReminder.time = new SimpleDateFormat("HH:mm").format(new Date());
            updateUI();
        }
    }

    public void setEditableReminder(Reminder reminder) {
        editableReminder = reminder;
        updateUI();
    }

    public LiveData<Reminder> getLoadedReminder() {
        return loadedReminder;
    }

    private void updateUI() {
        titleField.setValue(editableReminder.title);
        dateField.setValue(editableReminder.date);
        timeField.setValue(editableReminder.time);
    }

    public void saveReminder(boolean isEdit) {
        editableReminder.title = titleField.getValue();
        editableReminder.date = dateField.getValue();
        editableReminder.time = timeField.getValue();

        if (isEdit) {
            reminderRepository.update(editableReminder);
            AlarmManagerUtil.cancelAlarm(getApplication().getApplicationContext(), alarmManager, editableReminder);
            AlarmManagerUtil.createAlarm(getApplication().getApplicationContext(), alarmManager, editableReminder);
        } else {
            reminderRepository.insert(editableReminder);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new CreateNewAlarmJob(editableReminder.reminderId));
            executorService.shutdown();
        }

    }

    public void deleteReminder() {
        reminderRepository.delete(editableReminder);
        AlarmManagerUtil.cancelAlarm(getApplication().getApplicationContext(), alarmManager, editableReminder);
    }

    public void setDateField(int year, int month, int day) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat parseFormat = new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd/MM/yyyy");
        String dateString = dateField.getValue();
        try {
            Date date = parseFormat.parse(String.valueOf(day) + "-" + String.valueOf(month+1) + "-" + String.valueOf(year));
            dateString = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateField.setValue(dateString);
    }

    public void setTimeField(int hour, int minute) {
        String timeString = String.format("%02d:%02d", hour, minute);
        timeField.setValue(timeString);
    }

    class CreateNewAlarmJob implements Runnable {
        private final String reminderId;

        public CreateNewAlarmJob(String reminderId) {
            this.reminderId = reminderId;
        }

        @Override
        public void run() {
            Reminder reminder = reminderRepository.getLatestReminder();
            while (!reminder.reminderId.equals(this.reminderId)) {
                reminder = reminderRepository.getLatestReminder();
            }
            AlarmManagerUtil.createAlarm(getApplication().getApplicationContext(), alarmManager, reminder);
        }
    }
}