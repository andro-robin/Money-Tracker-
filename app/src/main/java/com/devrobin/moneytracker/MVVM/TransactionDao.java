package com.devrobin.moneytracker.MVVM;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.devrobin.moneytracker.MVVM.Model.TransactionModel;

import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    void insertTransaction(TransactionModel transModel);

    @Update
    void updateTransaction(TransactionModel transModel);

    @Delete
    void deleteTransaction(TransactionModel transModel);

    @Query("SELECT * FROM transaction_table")
    LiveData<List<TransactionModel>> getAllTransaction();

}
