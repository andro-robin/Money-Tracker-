package com.devrobin.moneytracker.MVVM.Repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.devrobin.moneytracker.MVVM.Model.CategoryChartData;
import com.devrobin.moneytracker.MVVM.Model.TransactionModel;
import com.devrobin.moneytracker.MVVM.TransactionDao;
import com.devrobin.moneytracker.MVVM.TransactionDatabase;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.DailyChartData;
import utils.DailySummer;
import utils.MonthlyChartData;
import utils.MonthlySummary;
import utils.YearlyChartData;

public class TransRepository{

    private TransactionDao transDao;

    private LiveData<List<TransactionModel>> allTransaction;

    public TransRepository(Application application){

        TransactionDatabase database = TransactionDatabase.getInstance(application);

        transDao = database.transDao();
        allTransaction = transDao.getAllTransaction();
    }


    public LiveData<List<TransactionModel>> getTransactionsByDate(Date date){

        return transDao.getTransactionByDate(date.getTime());
    }


    public LiveData<DailySummer> getDailySummer(Date date){
        return transDao.getDailySummery(date.getTime());
    }

    public LiveData<MonthlySummary> getMonthlySummer(Date date){
        return transDao.getMonthlySummary(date.getTime());
    }

    public LiveData<List<TransactionModel>> getAllTransaction(){
        return transDao.getAllTransaction();
    }


    //Chart Methods
    public LiveData<List<CategoryChartData>> getCategoryChartData(Date date) {
        return transDao.getCategoryChartData(date.getTime());
    }

    public LiveData<List<DailyChartData>> getDailyChartData(Date date) {
        return transDao.getDailyChartData(date.getTime());
    }

    public LiveData<List<MonthlyChartData>> getMonthlyChartData(Date date) {
        return transDao.getMonthlyChartData(date.getTime());
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
