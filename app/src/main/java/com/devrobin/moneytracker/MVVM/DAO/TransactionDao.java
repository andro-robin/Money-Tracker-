package com.devrobin.moneytracker.MVVM.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.devrobin.moneytracker.MVVM.Model.CategoryChartData;
import com.devrobin.moneytracker.MVVM.Model.TransactionModel;


import java.util.List;

import utils.DailyChartData;
import utils.DailySummer;
import utils.MonthlyChartData;
import utils.MonthlySummary;

@Dao
public interface TransactionDao {

    @Insert
    void insertTransaction(TransactionModel transModel);

    @Update
    void updateTransaction(TransactionModel transModel);

    @Delete
    void deleteTransaction(TransactionModel transModel);


    //Transaction for specific date
    @Query("SELECT * FROM transaction_table WHERE DATE(transactionDate/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch') ORDER BY transactionDate DESC, createDate DESC")
    LiveData<List<TransactionModel>> getTransactionByDate(long date);

    //Transaction for specific date (synchronous)
    @Query("SELECT * FROM transaction_table WHERE DATE(transactionDate/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch') ORDER BY transactionDate DESC, createDate DESC")
    List<TransactionModel> getTransactionByDateSync(long date);

    //Transaction for specific month (synchronous)
    @Query("SELECT * FROM transaction_table WHERE strftime('%Y-%m', transactionDate/1000, 'unixepoch') = strftime('%Y-%m', :date/1000, 'unixepoch') ORDER BY transactionDate DESC, createDate DESC")
    List<TransactionModel> getTransactionByMonthSync(long date);

    //Daily Summery for Specific Date
    @Query("SELECT " +
            "COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END), 0) as totalIncome, " +
            "COALESCE(SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END), 0) as totalExpense," +
            "COUNT(*) transactionCount " +
            "FROM transaction_table " +
            "WHERE DATE(transactionDate/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch')")
    LiveData<DailySummer> getDailySummery(long date);


    //Get Monthly Summary for a month
    @Query("SELECT " +
            "COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END), 0) as monthlyIncome, " +
            "COALESCE(SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END), 0) as monthlyExpense, " +
            "COUNT(*) monthlyTransaction " +
            "FROM transaction_table " +
            "WHERE strftime('%Y-%m', transactionDate/1000, 'unixepoch') = strftime('%Y-%m', :date/1000, 'unixepoch')")
    LiveData<MonthlySummary> getMonthlySummary(long date);

    @Query("SELECT * FROM transaction_table ORDER BY transactionDate DESC, createDate DESC")
    LiveData<List<TransactionModel>> getAllTransaction();

    @Query("DELETE FROM transaction_table")
    void deleteAllTransactions();


////    / Daily chart data for current month
//    @Query("SELECT " +
//            "DATE(transactionDate/1000, 'unixepoch') as date, " +
//            "COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END), 0) as income, " +
//            "COALESCE(SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END), 0) as expense " +
//            "FROM transaction_table " +
//            "WHERE strftime('%Y-%m', transactionDate/1000, 'unixepoch') = strftime('%Y-%m', :date/1000, 'unixepoch') " +
//            "GROUP BY DATE(transactionDate/1000, 'unixepoch') " +
//            "ORDER BY DATE(transactionDate/1000, 'unixepoch')")
//    LiveData<List<DailyChartData>> getDailyChartData(long date);
//
//    // Monthly chart data for current year
//    @Query("SELECT " +
//            "strftime('%Y-%m', transactionDate/1000, 'unixepoch') as month, " +
//            "COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END), 0) as income, " +
//            "COALESCE(SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END), 0) as expense " +
//            "FROM transaction_table " +
//            "WHERE strftime('%Y', transactionDate/1000, 'unixepoch') = strftime('%Y', :date/1000, 'unixepoch') " +
//            "GROUP BY strftime('%Y-%m', transactionDate/1000, 'unixepoch') " +
//            "ORDER BY strftime('%Y-%m', transactionDate/1000, 'unixepoch')")
//    LiveData<List<MonthlyChartData>> getMonthlyChartData(long date);
//
//    // Yearly chart data
//    @Query("SELECT " +
//            "strftime('%Y', transactionDate/1000, 'unixepoch') as year, " +
//            "COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END), 0) as income, " +
//            "COALESCE(SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END), 0) as expense " +
//            "FROM transaction_table " +
//            "GROUP BY strftime('%Y', transactionDate/1000, 'unixepoch') " +
//            "ORDER BY strftime('%Y', transactionDate/1000, 'unixepoch')")
//    LiveData<List<YearlyChartData>> getYearlyChartData();


// Category wise data for pie chart
@Query("SELECT category, " +
        "SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) as income, " +
        "SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) as expense " +
        "FROM transaction_table " +
        "WHERE strftime('%Y-%m', transactionDate/1000, 'unixepoch') = strftime('%Y-%m', :date/1000, 'unixepoch') " +
        "GROUP BY category " +
        "HAVING (income > 0 OR expense > 0) " +
        "ORDER BY (income + expense) DESC")
LiveData<List<CategoryChartData>> getCategoryChartData(long date);

    // Daily data for current month
    @Query("SELECT DATE(transactionDate/1000, 'unixepoch') as date, " +
            "SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) as income, " +
            "SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) as expense " +
            "FROM transaction_table " +
            "WHERE strftime('%Y-%m', transactionDate/1000, 'unixepoch') = strftime('%Y-%m', :date/1000, 'unixepoch') " +
            "GROUP BY DATE(transactionDate/1000, 'unixepoch') " +
            "HAVING (income > 0 OR expense > 0) " +
            "ORDER BY DATE(transactionDate/1000, 'unixepoch')")
    LiveData<List<DailyChartData>> getDailyChartData(long date);

    // Monthly data for current year
    @Query("SELECT strftime('%Y-%m', transactionDate/1000, 'unixepoch') as month, " +
            "SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) as income, " +
            "SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) as expense " +
            "FROM transaction_table " +
            "WHERE strftime('%Y', transactionDate/1000, 'unixepoch') = strftime('%Y', :date/1000, 'unixepoch') " +
            "GROUP BY strftime('%Y-%m', transactionDate/1000, 'unixepoch') " +
            "HAVING (income > 0 OR expense > 0) " +
            "ORDER BY strftime('%Y-%m', transactionDate/1000, 'unixepoch')")
    LiveData<List<MonthlyChartData>> getMonthlyChartData(long date);

    // Get transactions by date range (synchronous)
    @Query("SELECT * FROM transaction_table WHERE transactionDate BETWEEN :startDate AND :endDate ORDER BY transactionDate DESC, createDate DESC")
    List<TransactionModel> getTransactionByDateRange(long startDate, long endDate);
}