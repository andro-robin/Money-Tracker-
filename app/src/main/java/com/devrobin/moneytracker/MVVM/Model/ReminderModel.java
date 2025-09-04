package com.devrobin.moneytracker.MVVM.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "reminder_table")
public class ReminderModel {

    @PrimaryKey(autoGenerate = true)
    private int reminderId;

    @ColumnInfo(name = "reminderName")
    private String reminderName;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "frequency")
    private String frequency; // Daily, Monthly, Yearly

    @ColumnInfo(name = "startDate")
    private long startDate;

    @ColumnInfo(name = "reminderTime")
    private long reminderTime; // Time in milliseconds

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "isActive")
    private boolean isActive;

    @ColumnInfo(name = "createdDate")
    private long createdDate;

    public ReminderModel() {
        this.createdDate = System.currentTimeMillis();
        this.isActive = true;
    }

    public ReminderModel(String reminderName, String category, String frequency, long startDate, long reminderTime, String note) {
        this.reminderName = reminderName;
        this.category = category;
        this.frequency = frequency;
        this.startDate = startDate;
        this.reminderTime = reminderTime;
        this.note = note;
        this.createdDate = System.currentTimeMillis();
        this.isActive = true;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    // Helper methods
    public String getFormattedTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reminderTime);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String amPm = hour >= 12 ? "PM" : "AM";
        hour = hour % 12;
        if (hour == 0) hour = 12;

        return String.format("%d:%02d %s", hour, minute, amPm);
    }

    public String getFormattedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        return String.format("%02d/%02d/%d", day, month, year);
    }

    public long getNextReminderTime() {
        // Start with today's date
        Calendar calendar = Calendar.getInstance();

        // Set the time from reminderTime
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(reminderTime);
        calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long currentTime = System.currentTimeMillis();

        // If the time has already passed today, schedule for next occurrence
        if (calendar.getTimeInMillis() <= currentTime) {
            switch (frequency.toLowerCase()) {
                case "daily":
                    // Add one day to get tomorrow at the same time
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    break;
                case "monthly":
                    // Add one month to get next month at the same time
                    calendar.add(Calendar.MONTH, 1);
                    break;
                case "yearly":
                    // Add one year to get next year at the same time
                    calendar.add(Calendar.YEAR, 1);
                    break;
            }
        }

        return calendar.getTimeInMillis();
    }
}
