package com.devrobin.moneytracker.MVVM.Model;

public class DateHeaderModel {

    private String date;
    private double totalExpenses;
    private double totalIncomes;

    public DateHeaderModel() {
    }

    public DateHeaderModel(String date, double totalExpenses, double totalIncomes) {
        this.date = date;
        this.totalExpenses = totalExpenses;
        this.totalIncomes = totalIncomes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public double getTotalIncomes() {
        return totalIncomes;
    }

    public void setTotalIncomes(double totalIncomes) {
        this.totalIncomes = totalIncomes;
    }
}
