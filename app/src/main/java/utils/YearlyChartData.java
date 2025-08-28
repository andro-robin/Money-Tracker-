package utils;

public class YearlyChartData {

    private String year;
    private double income;
    private double expense;

    public YearlyChartData(String year, double income, double expense) {
        this.year = year;
        this.income = income;
        this.expense = expense;
    }


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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
