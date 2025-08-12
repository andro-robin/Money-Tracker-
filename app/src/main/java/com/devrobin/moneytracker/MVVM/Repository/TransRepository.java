package com.devrobin.moneytracker.MVVM.Repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.devrobin.moneytracker.MVVM.Model.TransactionModel;
import com.devrobin.moneytracker.MVVM.TransactionDao;
import com.devrobin.moneytracker.MVVM.TransactionDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransRepository{

    private TransactionDao transDao;

    private LiveData<List<TransactionModel>> Transaction;

    public TransRepository(Application application){

        TransactionDatabase database = TransactionDatabase.getInstance(application);

        transDao = database.transDao();
    }

    public LiveData<List<TransactionModel>> getTransaction(){
        return transDao.getAllTransaction();
    }


    //Insert Transaction
    public void InsertTrans(TransactionModel transModel){

        ExecutorService executors = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.myLooper());

        executors.execute(new Runnable() {
            @Override
            public void run() {
                transDao.insertTransaction(transModel);
            }
        });

    }


    //Update Transaction
    public void UpdateTrans(TransactionModel transModel){

        ExecutorService executors = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.myLooper());

        executors.execute(new Runnable() {
            @Override
            public void run() {
                transDao.updateTransaction(transModel);
            }
        });

    }


    //Delete Transaction
    public void DeleteTrans(TransactionModel transModel){

        ExecutorService executors = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.myLooper());

        executors.execute(new Runnable() {
            @Override
            public void run() {
                transDao.deleteTransaction(transModel);
            }
        });

    }

}
