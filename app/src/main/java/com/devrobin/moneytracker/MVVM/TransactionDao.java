package com.devrobin.moneytracker.MVVM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.devrobin.moneytracker.MVVM.Model.TransactionModel;


import java.util.List;

import utils.DailySummer;
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

}
