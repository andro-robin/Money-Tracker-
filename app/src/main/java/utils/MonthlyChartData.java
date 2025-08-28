package utils;

public class MonthlyChartData {

    private String month;
    private double income;
    private double expense;

    public MonthlyChartData(String month, double income, double expense) {
        this.month = month;
        this.income = income;
        this.expense = expense;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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
}
