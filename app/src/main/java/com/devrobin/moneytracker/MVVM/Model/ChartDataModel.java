package com.devrobin.moneytracker.MVVM.Model;

public class ChartDataModel {

    private String label;
    private double income;
    private double expense;
    private String date;

    public ChartDataModel(String label, double income, double expense, String date) {
        this.label = label;
        this.income = income;
        this.expense = expense;
        this.date = date;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getBalance(){
        return income - expense;
    }
}
