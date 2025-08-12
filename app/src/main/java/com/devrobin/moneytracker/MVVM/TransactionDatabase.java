package com.devrobin.moneytracker.MVVM;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.devrobin.moneytracker.MVVM.Model.TransactionModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.Constant;

@Database(entities = {TransactionModel.class}, version = 1)
public abstract class TransactionDatabase extends RoomDatabase {

    public abstract TransactionDao transDao();

    //SingleTon Pattern
    private static TransactionDatabase instance;

    public static synchronized TransactionDatabase getInstance(Context context){

        if (instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    TransactionDatabase.class,
                    "transaction_table")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }


    private static final RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            initialTransaction();

        }
    };



    private static void initialTransaction() {

        TransactionDao transDao = instance.transDao();


        ExecutorService executorService = Executors.newSingleThreadExecutor();


        executorService.execute(new Runnable() {
            @Override
            public void run() {

//                TransactionModel trans1 = new TransactionModel();
//
//                trans1.setType(Constant.EXPENSE);
//                trans1.setCategory("Salary");
//                trans1.setAmount(50);
//
//                TransactionModel trans2 = new TransactionModel();
//
//                trans2.setType(Constant.INCOME);
//                trans2.setCategory("Business");
//                trans2.setAmount(800);
//
//
//                transDao.insertTransaction(trans1);
//                transDao.insertTransaction(trans2);
            }
        });

    }

}

