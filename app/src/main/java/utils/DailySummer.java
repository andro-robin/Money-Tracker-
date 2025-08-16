package utils;

import java.util.Calendar;
import java.util.Date;

public class DailySummer {

    private double totalIncome;
    private double totalExpense;
    private int transactionCount;

    public DailySummer() {
    }

    public DailySummer(double totalIncome, double totalExpense, int transactionCount) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.transactionCount = transactionCount;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }


    //Calculate  total Balance Dynamically just method: No need Field
    public double getTotalBalance() {
        return totalIncome - totalExpense;
    }
}
