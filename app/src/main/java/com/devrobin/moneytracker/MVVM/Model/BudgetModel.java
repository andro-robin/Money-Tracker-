package com.devrobin.moneytracker.MVVM.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "budget_table")
public class BudgetModel {

    @PrimaryKey(autoGenerate = true)
    private int budgetId;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "budgetType")
    private String budgetType; // This corresponds to frequency (Daily, Monthly, Yearly)

    @ColumnInfo(name = "budgetAmount")
    private double budgetAmount; // This corresponds to amount

    @ColumnInfo(name = "spentAmount")
    private double spentAmount; // This corresponds to spent

    @ColumnInfo(name = "month")
    private int month;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "day")
    private int day;

    @ColumnInfo(name = "note")
    private String note;

    public BudgetModel() {
    }

    public BudgetModel(String category, String budgetType, double budgetAmount) {
        this.category = category;
        this.budgetType = budgetType;
        this.budgetAmount = budgetAmount;
        this.spentAmount = 0.0;

        // Set current date
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        this.month = calendar.get(java.util.Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
        this.year = calendar.get(java.util.Calendar.YEAR);
        this.day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
    }

    public BudgetModel(int budgetId, String category, String budgetType, double budgetAmount, double spentAmount, int month, int year, int day) {
        this.budgetId = budgetId;
        this.category = category;
        this.budgetType = budgetType;
        this.budgetAmount = budgetAmount;
        this.spentAmount = spentAmount;
        this.month = month;
        this.year = year;
        this.day = day;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    // Convenience method to get frequency (for backward compatibility)
    public String getFrequency() {
        return budgetType;
    }

    // Convenience method to set frequency (for backward compatibility)
    public void setFrequency(String frequency) {
        this.budgetType = frequency;
    }

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    // Convenience method to get amount (for backward compatibility)
    public double getAmount() {
        return budgetAmount;
    }

    // Convenience method to set amount (for backward compatibility)
    public void setAmount(double amount) {
        this.budgetAmount = amount;
    }

    public double getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(double spentAmount) {
        this.spentAmount = spentAmount;
    }

    // Convenience method to get spent (for backward compatibility)
    public double getSpent() {
        return spentAmount;
    }

    // Convenience method to set spent (for backward compatibility)
    public void setSpent(double spent) {
        this.spentAmount = spent;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getRemaining() {
        return budgetAmount - spentAmount;
    }

    public double getProgressPercentage() {
        if (budgetAmount == 0) return 0;
        return (spentAmount / budgetAmount) * 100;
    }

    // Convenience method to get createDate (for backward compatibility)
    public long getCreateDate() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(year, month - 1, day); // month is 0-based in Calendar
        return calendar.getTimeInMillis();
    }

    // Convenience method to set createDate (for backward compatibility)
    public void setCreateDate(long createDate) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(createDate);
        this.year = calendar.get(java.util.Calendar.YEAR);
        this.month = calendar.get(java.util.Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
        this.day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
    }
}
