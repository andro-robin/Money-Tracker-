package com.devrobin.moneytracker.MVVM;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.devrobin.moneytracker.MVVM.DAO.AccountDAO;
import com.devrobin.moneytracker.MVVM.DAO.BudgetDAO;
import com.devrobin.moneytracker.MVVM.DAO.CategoryDAO;
import com.devrobin.moneytracker.MVVM.DAO.ReminderDAO;
import com.devrobin.moneytracker.MVVM.DAO.TransactionDao;
import com.devrobin.moneytracker.MVVM.Model.AccountModel;
import com.devrobin.moneytracker.MVVM.Model.BudgetModel;
import com.devrobin.moneytracker.MVVM.Model.CategoryModel;
import com.devrobin.moneytracker.MVVM.Model.ReminderModel;
import com.devrobin.moneytracker.MVVM.Model.TransactionModel;
import com.devrobin.moneytracker.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.DateConverter;

@Database(entities = {TransactionModel.class, AccountModel.class, BudgetModel.class, ReminderModel.class, CategoryModel.class},
        version = 2, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class TransactionDatabase extends RoomDatabase {

    public abstract TransactionDao transDao();
    public abstract AccountDAO accountDAO();
    public abstract BudgetDAO budgetDAO();
    public abstract ReminderDAO reminderDAO();
    public abstract CategoryDAO categoryDAO();



    //SingleTon Pattern
    private static TransactionDatabase instance;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

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

        AccountDAO accountDao = instance.accountDAO();
        TransactionDao transDao = instance.transDao();
        BudgetDAO budgetDao = instance.budgetDAO();
        ReminderDAO reminderDao = instance.reminderDAO();
        CategoryDAO categoryDAO = instance.categoryDAO();

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
                  BudgetModel foodBudget = new BudgetModel("Food", "Daily", 200.0);
                  budgetDao.insertBudget(foodBudget);

                // Create sample reminders
                long currentTime = System.currentTimeMillis();
                  ReminderModel sampleReminder1 = new ReminderModel("Cost of Car", "Transport", "Daily", currentTime, currentTime, "Don't forget to note your car expenses");
                  ReminderModel sampleReminder2 = new ReminderModel("Cost of Transport", "Transport", "Monthly", currentTime, currentTime, "Monthly transport expense reminder");

                reminderDao.insertReminder(sampleReminder1);
                reminderDao.insertReminder(sampleReminder2);



                //  Create default categories with proper drawable resource IDs
                CategoryModel salary = new CategoryModel("Salary", R.drawable.salary, true);
                CategoryModel Business = new CategoryModel("Business", R.drawable.investment, true);
                CategoryModel Shopping = new CategoryModel("Shopping", R.drawable.shopping, true);
                CategoryModel Bike = new CategoryModel("Bike", R.drawable.bike, true);
                CategoryModel Travel = new CategoryModel("Travel", R.drawable.travel, true);
                CategoryModel Rent = new CategoryModel("Rent", R.drawable.rent, true);
                CategoryModel Electronics = new CategoryModel("Electronics", R.drawable.electronics, true);
                CategoryModel Clothing = new CategoryModel("Clothing", R.drawable.clothing, true);
                CategoryModel Health = new CategoryModel("Health", R.drawable.health, true);
                CategoryModel Pet = new CategoryModel("Pet", R.drawable.pet, true);
                CategoryModel Gifts = new CategoryModel("Gifts", R.drawable.gift, true);
                CategoryModel Phone = new CategoryModel("Phone", R.drawable.phone, true);
                CategoryModel Beauty = new CategoryModel("Beauty", R.drawable.beauty, true);
                CategoryModel Social = new CategoryModel("Social", R.drawable.social, true);
                CategoryModel Sport = new CategoryModel("Sport", R.drawable.sports, true);
                CategoryModel Housing = new CategoryModel("House", R.drawable.house, true);
                CategoryModel Marketing = new CategoryModel("Market", R.drawable.market, true);
                CategoryModel Others = new CategoryModel("Others", R.drawable.others, true);


                categoryDAO.insertCategory(salary);
                categoryDAO.insertCategory(Business);
                categoryDAO.insertCategory(Shopping);
                categoryDAO.insertCategory(Bike);
                categoryDAO.insertCategory(Travel);
                categoryDAO.insertCategory(Rent);
                categoryDAO.insertCategory(Electronics);
                categoryDAO.insertCategory(Clothing);
                categoryDAO.insertCategory(Health);
                categoryDAO.insertCategory(Pet);
                categoryDAO.insertCategory(Gifts);
                categoryDAO.insertCategory(Phone);
                categoryDAO.insertCategory(Beauty);
                categoryDAO.insertCategory(Social);
                categoryDAO.insertCategory(Sport);
                categoryDAO.insertCategory(Housing);
                categoryDAO.insertCategory(Marketing);
                categoryDAO.insertCategory(Others);

            }
        });
    }
}



