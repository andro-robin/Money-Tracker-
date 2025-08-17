package utils;

public class MonthlySummary {

    private double monthlyIncome;
    private double monthlyExpense;
    private int monthlyTransaction;

    public MonthlySummary() {
    }

    public MonthlySummary(double monthlyIncome, double monthlyExpense, int monthlyTransaction) {
        this.monthlyIncome = monthlyIncome;
        this.monthlyExpense = monthlyExpense;
        this.monthlyTransaction = monthlyTransaction;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public double getMonthlyExpense() {
        return monthlyExpense;
    }

    public void setMonthlyExpense(double monthlyExpense) {
        this.monthlyExpense = monthlyExpense;
    }

    public int getMonthlyTransaction() {
        return monthlyTransaction;
    }

    public void setMonthlyTransaction(int monthlyTransaction) {
        this.monthlyTransaction = monthlyTransaction;
    }

    public double getMonthlyBalance(){
        return monthlyIncome - monthlyExpense;
    }
}
