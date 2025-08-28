package com.devrobin.moneytracker.MVVM.Model;

public class CategoryChartData {
    private String category;
    private double income;
    private double expense;

    public CategoryChartData() {}

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getIncome() { return income; }
    public void setIncome(double income) { this.income = income; }

    public double getExpense() { return expense; }
    public void setExpense(double expense) { this.expense = expense; }

    public double getTotal() { return income + expense; }
}