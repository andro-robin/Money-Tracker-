package com.devrobin.moneytracker.MVVM;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.devrobin.moneytracker.MVVM.DAO.AccountDAO;
import com.devrobin.moneytracker.MVVM.DAO.TransactionDao;
import com.devrobin.moneytracker.MVVM.Model.AccountModel;
import com.devrobin.moneytracker.MVVM.Model.TransactionModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.DateConverter;

@Database(entities = {TransactionModel.class, AccountModel.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class TransactionDatabase extends RoomDatabase {

    public abstract TransactionDao transDao();

    public abstract AccountDAO accountDAO();


    //SingleTon Pattern
    private static TransactionDatabase instance;

    public static synchronized TransactionDatabase getInstance(Context context) {

        if (instance == null) {
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

//                        .addCallback(roomCallback)


   private static final RoomDatabase.Callback roomCallback = new Callback() {
       @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
           super.onCreate(db);

           initialTransaction();
        }
    };


    private static void initialTransaction() {
//
//        TransactionDao transDao = instance.transDao();
//
//
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//
//
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//
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
//            }
//        });
//
//    }

        AccountDAO accountDao = instance.accountDAO();
        TransactionDao transDao = instance.transDao();
        //BudgetDao budgetDao = instance.budgetDao();
        //ReminderDao reminderDao = instance.reminderDao();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                // Create default accounts with new fields
                AccountModel cashAccount = new AccountModel("Cash", "Cash", "BDT", 0.0);
                AccountModel bankAccount = new AccountModel("Bank Account", "Bank Account", "BDT", 0.0);
                AccountModel creditCard = new AccountModel("Credit Card", "Credit Card", "BDT", 0.0);
                AccountModel debitCard = new AccountModel("Debit Card", "Debit Card", "BDT", 0.0);

                accountDao.insertAccount(cashAccount);
                accountDao.insertAccount(bankAccount);
                accountDao.insertAccount(creditCard);
                accountDao.insertAccount(debitCard);

                // Create sample budget
                //  BudgetModel foodBudget = new BudgetModel("Food", "Daily", 200.0);
                //  budgetDao.insertBudget(foodBudget);

                // Create sample reminders
                long currentTime = System.currentTimeMillis();
                //  ReminderModel sampleReminder1 = new ReminderModel("Cost of Car", "Transport", "Daily", currentTime, currentTime, "Don't forget to note your car expenses");
                //  ReminderModel sampleReminder2 = new ReminderModel("Cost of Transport", "Transport", "Monthly", currentTime, currentTime, "Monthly transport expense reminder");

                //reminderDao.insertReminder(sampleReminder1);
                //reminderDao.insertReminder(sampleReminder2);
            }
        });
    }
}



