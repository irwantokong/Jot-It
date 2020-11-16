package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Reminder;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.ReminderRepository;

public class EditReminderViewModel extends AndroidViewModel {
    private ReminderRepository reminderRepository;
    private Reminder editableReminder;

    public MutableLiveData<String> _titleField = new MutableLiveData<>();
    public MutableLiveData<String> _dateField = new MutableLiveData<>();
    public MutableLiveData<String> _timeField = new MutableLiveData<>();

    public EditReminderViewModel(Application application) {
        super(application);
        reminderRepository = new ReminderRepository(application);
    }

    public void initReminder() {
        editableReminder = new Reminder();
        editableReminder.date = new SimpleDateFormat("E, dd/MM/yyyy").format(new Date());
        editableReminder.time = new SimpleDateFormat("HH:mm").format(new Date());

        _dateField.setValue(editableReminder.date);
        _timeField.setValue(editableReminder.time);
    }

    public void saveReminder() {
        editableReminder.title = _titleField.getValue();
        editableReminder.date = _dateField.getValue();
        editableReminder.time = _timeField.getValue();
        reminderRepository.insert(editableReminder);
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
}