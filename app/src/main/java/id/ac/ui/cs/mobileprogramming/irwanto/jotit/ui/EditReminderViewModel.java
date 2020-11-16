package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Reminder;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.ReminderRepository;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.util.AlarmManagerUtil;

public class EditReminderViewModel extends AndroidViewModel {
    private ReminderRepository reminderRepository;
    private Reminder editableReminder;
    private AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);

    public MutableLiveData<String> _titleField = new MutableLiveData<>();
    public MutableLiveData<String> _dateField = new MutableLiveData<>();
    public MutableLiveData<String> _timeField = new MutableLiveData<>();

    public EditReminderViewModel(Application application) {
        super(application);
        reminderRepository = new ReminderRepository(application);
    }

    public void initReminder(String reminderId) {
        if (reminderId != null) {
            editableReminder = reminderRepository.getReminderById(reminderId);
        } else {
            editableReminder = new Reminder();
            editableReminder.date = new SimpleDateFormat("E, dd/MM/yyyy").format(new Date());
            editableReminder.time = new SimpleDateFormat("HH:mm").format(new Date());
        }

        _titleField.setValue(editableReminder.title);
        _dateField.setValue(editableReminder.date);
        _timeField.setValue(editableReminder.time);
    }

    public void saveReminder(String reminderId) {
        editableReminder.title = _titleField.getValue();
        editableReminder.date = _dateField.getValue();
        editableReminder.time = _timeField.getValue();
        if (reminderId != null) {
            reminderRepository.update(editableReminder);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new UpdateAlarmJob(editableReminder));
            executorService.shutdown();
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

    public void set_dateField(int year, int month, int day) {
        SimpleDateFormat parseFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd/MM/yyyy");
        String dateString = _dateField.getValue();
        try {
            Date date = parseFormat.parse(String.valueOf(day) + "-" + String.valueOf(month+1) + "-" + String.valueOf(year));
            dateString = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        _dateField.setValue(dateString);
    }

    public void set_timeField(int hour, int minute) {
        String timeString = String.format("%02d:%02d", hour, minute);
        _timeField.setValue(timeString);
    }

    class CreateNewAlarmJob implements Runnable {
        private String reminderId;

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

    class UpdateAlarmJob implements Runnable {
        private Reminder reminder;

        public UpdateAlarmJob(Reminder reminder) {
            this.reminder = reminder;
        }

        @Override
        public void run() {
            AlarmManagerUtil.cancelAlarm(getApplication().getApplicationContext(), alarmManager, this.reminder);
            AlarmManagerUtil.createAlarm(getApplication().getApplicationContext(), alarmManager, this.reminder);
        }
    }
}