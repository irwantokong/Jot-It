package id.ac.ui.cs.mobileprogramming.irwanto.jotit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Reminder;

@Dao
public interface ReminderDAO {
    @Query("SELECT * FROM reminders_table ORDER BY _id DESC")
    LiveData<List<Reminder>> getAllReminders();

    @Query("SELECT * FROM reminders_table WHERE reminder_id = :reminderId")
    LiveData<Reminder> getLivaDataReminderById(String reminderId);

    @Query("SELECT * FROM reminders_table WHERE reminder_id = :reminderId")
    Reminder getReminderById(String reminderId);

    @Insert
    void insertReminder(Reminder note);

    @Update
    void updateReminder(Reminder note);

    @Delete
    void deleteReminder(Reminder note);
}
