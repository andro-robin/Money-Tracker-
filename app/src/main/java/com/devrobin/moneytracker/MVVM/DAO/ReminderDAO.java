package com.devrobin.moneytracker.MVVM.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.devrobin.moneytracker.MVVM.Model.ReminderModel;

import java.util.List;

@Dao
public interface ReminderDAO {

    @Insert
    void insertReminder(ReminderModel reminderModel);

    @Update
    void updateReminder(ReminderModel reminderModel);

    @Delete
    void deleteReminder(ReminderModel reminderModel);

    @Query("SELECT * FROM reminder_table ORDER BY createdDate DESC")
    LiveData<List<ReminderModel>> getAllReminders();

    @Query("SELECT * FROM reminder_table WHERE isActive = 1 ORDER BY createdDate DESC")
    LiveData<List<ReminderModel>> getActiveReminders();

    @Query("SELECT * FROM reminder_table WHERE reminderId = :reminderId")
    LiveData<ReminderModel> getReminderById(int reminderId);

    @Query("SELECT * FROM reminder_table WHERE category = :category ORDER BY createdDate DESC")
    LiveData<List<ReminderModel>> getRemindersByCategory(String category);

    @Query("SELECT * FROM reminder_table WHERE frequency = :frequency ORDER BY createdDate DESC")
    LiveData<List<ReminderModel>> getRemindersByFrequency(String frequency);

    @Query("DELETE FROM reminder_table WHERE reminderId = :reminderId")
    void deleteReminderById(int reminderId);

    @Query("UPDATE reminder_table SET isActive = :isActive WHERE reminderId = :reminderId")
    void updateReminderStatus(int reminderId, boolean isActive);
}