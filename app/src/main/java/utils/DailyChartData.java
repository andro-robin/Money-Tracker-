package utils;

public class DailyChartData {

    private String date;
    private  double income;
    private double expense;


    public DailyChartData(String date, double income, double expense) {
        this.date = date;
        this.income = income;
        this.expense = expense;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
