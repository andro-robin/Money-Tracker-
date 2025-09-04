package com.devrobin.moneytracker.Views.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.devrobin.moneytracker.MVVM.MainViewModel.ReminderViewModel;
import com.devrobin.moneytracker.MVVM.Model.ReminderModel;
import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.ActivityAddRemindersBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import utils.NotificationHelper;

public class AddRemindersActivity extends AppCompatActivity {

    private ActivityAddRemindersBinding binding;
    private ReminderViewModel reminderViewModel;
    private ReminderModel currentReminder;
    private boolean isEditMode = false;
    private String selectedFrequency = "Daily";
    private Calendar selectedDate = Calendar.getInstance();
    private Calendar selectedTime = Calendar.getInstance();
    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAddRemindersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel and NotificationHelper
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        notificationHelper = new NotificationHelper(this);



        // Check if editing existing reminder
        int reminderId = getIntent().getIntExtra("reminder_id", -1);
        if (reminderId != -1) {
            isEditMode = true;
            loadReminder(reminderId);
        }

        setupViews();
        setupClickListeners();

    }

    private void setupViews() {
        // Set default values
        if (!isEditMode) {
            binding.tvSelectedFrequency.setText("Daily");
            binding.tvSelectedDate.setText(getFormattedDate(selectedDate));
            binding.tvSelectedTime.setText(getFormattedTime(selectedTime));
        }
    }

    private void setupClickListeners() {
        // Back button
        binding.btnBack.setOnClickListener(v -> onBackPressed());

        // Frequency dropdown
        binding.btnFrequencyDropdown.setOnClickListener(v -> toggleFrequencyDropdown());

        // Frequency options
        binding.optionDaily.setOnClickListener(v -> selectFrequency("Daily"));
        binding.optionMonthly.setOnClickListener(v -> selectFrequency("Monthly"));
        binding.optionYearly.setOnClickListener(v -> selectFrequency("Yearly"));

        // Date picker
        binding.btnDatePicker.setOnClickListener(v -> showDatePicker());

        // Time picker
        binding.btnTimePicker.setOnClickListener(v -> showTimePicker());

        // Save button
        binding.btnSave.setOnClickListener(v -> saveReminder());
    }

    private void toggleFrequencyDropdown() {
        if (binding.frequencyDropdown.getVisibility() == View.VISIBLE) {
            binding.frequencyDropdown.setVisibility(View.GONE);
        } else {
            binding.frequencyDropdown.setVisibility(View.VISIBLE);
        }
    }

    private void selectFrequency(String frequency) {
        selectedFrequency = frequency;
        binding.tvSelectedFrequency.setText(frequency);
        toggleFrequencyDropdown();
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
                    binding.tvSelectedDate.setText(getFormattedDate(selectedDate));
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedTime.set(Calendar.MINUTE, minute);
                    binding.tvSelectedTime.setText(getFormattedTime(selectedTime));
                },
                selectedTime.get(Calendar.HOUR_OF_DAY),
                selectedTime.get(Calendar.MINUTE),
                false // 24-hour format
        );
        timePickerDialog.show();
    }


    private String getFormattedDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private String getFormattedTime(Calendar calendar) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormat.format(calendar.getTime());
    }

    private void loadReminder(int reminderId) {
        reminderViewModel.getReminderById(reminderId).observe(this, reminderModel -> {
            if (reminderModel != null) {
                currentReminder = reminderModel;

                binding.etReminderName.setText(reminderModel.getReminderName());
                selectedFrequency = reminderModel.getFrequency();
                binding.tvSelectedFrequency.setText(selectedFrequency);

                selectedDate.setTimeInMillis(reminderModel.getStartDate());
                binding.tvSelectedDate.setText(getFormattedDate(selectedDate));

                selectedTime.setTimeInMillis(reminderModel.getReminderTime());
                binding.tvSelectedTime.setText(getFormattedTime(selectedTime));

                if (reminderModel.getNote() != null && !reminderModel.getNote().isEmpty()) {
                    binding.etNote.setText(reminderModel.getNote());
                }
            }
        });
    }

    private void saveReminder() {
        String reminderName = binding.etReminderName.getText().toString().trim();
        String note = binding.etNote.getText().toString().trim();

        // Validation
        if (reminderName.isEmpty()) {
            binding.etReminderName.setError("Please enter reminder name");
            return;
        }

        if (isEditMode && currentReminder != null) {
            // Cancel existing notification
            notificationHelper.cancelCustomReminder(currentReminder);

            // Update existing reminder
            currentReminder.setReminderName(reminderName);
            currentReminder.setFrequency(selectedFrequency);
            currentReminder.setStartDate(selectedDate.getTimeInMillis());
            currentReminder.setReminderTime(selectedTime.getTimeInMillis());
            currentReminder.setNote(note);

            reminderViewModel.updateReminder(currentReminder);

            // Schedule new notification
            notificationHelper.scheduleCustomReminder(currentReminder);

            Toast.makeText(this, "Reminder updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Create new reminder
            ReminderModel newReminder = new ReminderModel();
            newReminder.setReminderName(reminderName);
            newReminder.setFrequency(selectedFrequency);
            newReminder.setStartDate(selectedDate.getTimeInMillis());
            newReminder.setReminderTime(selectedTime.getTimeInMillis());
            newReminder.setNote(note);

            reminderViewModel.insertReminder(newReminder);

            // Schedule notification after reminder is saved
            reminderViewModel.getReminderById(newReminder.getReminderId()).observe(this, savedReminder -> {
                if (savedReminder != null) {
                    notificationHelper.scheduleCustomReminder(savedReminder);

                    // Debug: Show next reminder time
                    long nextTime = savedReminder.getNextReminderTime();
                    Calendar nextCal = Calendar.getInstance();
                    nextCal.setTimeInMillis(nextTime);
                    String debugMsg = String.format("Reminder scheduled for: %02d:%02d on %02d/%02d/%d",
                            nextCal.get(Calendar.HOUR_OF_DAY),
                            nextCal.get(Calendar.MINUTE),
                            nextCal.get(Calendar.DAY_OF_MONTH),
                            nextCal.get(Calendar.MONTH) + 1,
                            nextCal.get(Calendar.YEAR));
                    Toast.makeText(this, debugMsg, Toast.LENGTH_LONG).show();
                }
            });

            Toast.makeText(this, "Reminder added successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }


}