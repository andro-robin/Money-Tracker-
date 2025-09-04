package com.devrobin.moneytracker.MVVM.MainViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.devrobin.moneytracker.MVVM.Model.ReminderModel;
import com.devrobin.moneytracker.MVVM.Repository.ReminderRepository;

import java.util.List;

public class ReminderViewModel extends AndroidViewModel {

    private ReminderRepository reminderRepository;
    private LiveData<List<ReminderModel>> allReminders;

    public ReminderViewModel(Application application) {
        super(application);
        reminderRepository = new ReminderRepository(application);
        allReminders = reminderRepository.getAllReminders();
    }


    public LiveData<List<ReminderModel>> getAllReminders() {
        return allReminders;
    }

    public LiveData<List<ReminderModel>> getActiveReminders() {
        return reminderRepository.getActiveReminders();
    }

    public LiveData<ReminderModel> getReminderById(int reminderId) {
        return reminderRepository.getReminderById(reminderId);
    }

    public LiveData<List<ReminderModel>> getRemindersByCategory(String category) {
        return reminderRepository.getRemindersByCategory(category);
    }

    public LiveData<List<ReminderModel>> getRemindersByFrequency(String frequency) {
        return reminderRepository.getRemindersByFrequency(frequency);
    }



    public void insertReminder(ReminderModel reminderModel) {
        reminderRepository.insertReminder(reminderModel);
    }

    public void updateReminder(ReminderModel reminderModel) {
        reminderRepository.updateReminder(reminderModel);
    }

    public void deleteReminder(ReminderModel reminderModel) {
        reminderRepository.deleteReminder(reminderModel);
    }

    public void deleteReminderById(int reminderId) {
        reminderRepository.deleteReminderById(reminderId);
    }

    public void updateReminderStatus(int reminderId, boolean isActive) {
        reminderRepository.updateReminderStatus(reminderId, isActive);
    }

}
