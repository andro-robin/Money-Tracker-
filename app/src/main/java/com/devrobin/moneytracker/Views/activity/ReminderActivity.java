package com.devrobin.moneytracker.Views.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devrobin.moneytracker.MVVM.MainViewModel.ReminderViewModel;
import com.devrobin.moneytracker.MVVM.Model.ReminderModel;
import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.adapter.ReminderAdapter;
import com.devrobin.moneytracker.databinding.ActivityReminderBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import utils.NotificationHelper;

public class ReminderActivity extends AppCompatActivity {

    private ActivityReminderBinding reminderBinding;
    private ReminderViewModel reminderViewModel;
    private ReminderAdapter reminderAdapter;
    private ArrayList<ReminderModel> reminderList;
    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        reminderBinding = ActivityReminderBinding.inflate(getLayoutInflater());
        setContentView(reminderBinding.getRoot());

        // Initialize ViewModel and NotificationHelper
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        notificationHelper = new NotificationHelper(this);

        // Initialize RecyclerView
        reminderList = new ArrayList<>();
        reminderAdapter = new ReminderAdapter(this, reminderList, new ReminderAdapter.onReminderItemClickListener() {
            @Override
            public void reminderItemClick(ReminderModel reminderModel) {
                // Handle reminder item click - could open edit mode
                openAddReminderActivity(reminderModel);
            }
        });

        // Set long click listener for deletion
        reminderAdapter.setReminderLongClickListener(new ReminderAdapter.onReminderLongClickListener() {
            @Override
            public void onReminderLongClick(ReminderModel reminderModel) {
                showDeleteDialog(reminderModel);
            }
        });

        reminderBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminderBinding.recyclerView.setAdapter(reminderAdapter);

        reminderBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddReminderActivity(null);
            }
        });

        reminderBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        reminderBinding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testNotification();
            }
        });


        reminderViewModel.getAllReminders().observe(this, new Observer<List<ReminderModel>>() {
            @Override
            public void onChanged(List<ReminderModel> reminderModels) {

                reminderList.clear();

                if (reminderModels != null){
                    reminderList.addAll(reminderModels);
                }

                reminderAdapter.notifyDataSetChanged();
            }
        });

    }

    private void openAddReminderActivity(ReminderModel reminderModel) {
        Intent intent = new Intent(this, AddRemindersActivity.class);
        if (reminderModel != null) {
            intent.putExtra("reminder_id", reminderModel.getReminderId());
        }
        startActivity(intent);
    }



    private void showDeleteDialog(ReminderModel reminderModel) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Reminder")
                .setMessage("Are you sure you want to delete '" + reminderModel.getReminderName() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Cancel the notification first
                    notificationHelper.cancelCustomReminder(reminderModel);

                    // Delete the reminder
                    reminderViewModel.deleteReminder(reminderModel);
                    Toast.makeText(this, "Reminder deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    // Test method for debugging notifications
    private void testNotification() {
        try {
            // Send immediate test notification
            notificationHelper.sendTestNotification();

            // Also schedule a test reminder for 1 minute from now
            ReminderModel testReminder = new ReminderModel();
            testReminder.setReminderId(9999);
            testReminder.setReminderName("Test Reminder");
            testReminder.setFrequency("Daily");
            testReminder.setActive(true);

            // Set time to 1 minute from now
            Calendar testTime = Calendar.getInstance();
            testTime.add(Calendar.MINUTE, 1);
            testReminder.setStartDate(testTime.getTimeInMillis());
            testReminder.setReminderTime(testTime.getTimeInMillis());

            notificationHelper.scheduleCustomReminder(testReminder);

            Toast.makeText(this, "Test notification sent and reminder scheduled for 1 minute from now!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("RemindersActivity", "Error in test notification", e);
            Toast.makeText(this, "Error testing notification: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}